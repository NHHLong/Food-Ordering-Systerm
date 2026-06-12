using FoodOrderWeb.Models;
using Microsoft.Data.SqlClient;

namespace FoodOrderWeb.DAL;

public class FoodRepository
{
    private readonly DbContext _db;

    public FoodRepository(DbContext db)
    {
        _db = db;
    }

    public List<Food> GetAll(string? keyword = null, int? categoryId = null)
    {
        var foods = new List<Food>();
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
SELECT f.FoodId, f.Name, f.Price, f.Status, f.Image, f.CategoryId, f.Description, f.IsDeleted, f.CreatedAt,
       COALESCE(c.Name, f.Category, '') AS CategoryName
FROM Foods f
LEFT JOIN Categories c ON f.CategoryId = c.CategoryId
WHERE f.IsDeleted = 0
  AND (@keyword IS NULL OR f.Name LIKE @keyword)
  AND (@categoryId IS NULL OR f.CategoryId = @categoryId)
ORDER BY f.FoodId";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@keyword", string.IsNullOrWhiteSpace(keyword) ? DBNull.Value : $"%{keyword}%");
        cmd.Parameters.AddWithValue("@categoryId", categoryId.HasValue ? categoryId.Value : DBNull.Value);

        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            foods.Add(Map(reader));
        }

        return foods;
    }

    public Food? GetById(int foodId)
    {
        return GetAll().FirstOrDefault(f => f.FoodId == foodId);
    }

    public void Save(Food food)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        var query = food.FoodId == 0
            ? @"INSERT INTO Foods (Name, Price, Status, Image, CategoryId, Description, IsDeleted, CreatedAt)
                VALUES (@name, @price, @status, @image, @categoryId, @description, 0, GETDATE())"
            : @"UPDATE Foods SET Name = @name, Price = @price, Status = @status, Image = @image,
                CategoryId = @categoryId, Description = @description WHERE FoodId = @foodId";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@foodId", food.FoodId);
        cmd.Parameters.AddWithValue("@name", food.Name);
        cmd.Parameters.AddWithValue("@price", food.Price);
        cmd.Parameters.AddWithValue("@status", food.Status);
        cmd.Parameters.AddWithValue("@image", (object?)food.Image ?? DBNull.Value);
        cmd.Parameters.AddWithValue("@categoryId", (object?)food.CategoryId ?? DBNull.Value);
        cmd.Parameters.AddWithValue("@description", (object?)food.Description ?? DBNull.Value);
        cmd.ExecuteNonQuery();
    }

    public void Delete(int foodId)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        using var cmd = new SqlCommand("UPDATE Foods SET IsDeleted = 1 WHERE FoodId = @foodId", conn);
        cmd.Parameters.AddWithValue("@foodId", foodId);
        cmd.ExecuteNonQuery();
    }

    private static Food Map(SqlDataReader reader)
    {
        return new Food
        {
            FoodId = (int)reader["FoodId"],
            Name = reader["Name"].ToString() ?? string.Empty,
            Price = (decimal)reader["Price"],
            Status = reader["Status"].ToString() ?? "Available",
            Image = reader["Image"] as string,
            CategoryId = reader["CategoryId"] == DBNull.Value ? null : (int)reader["CategoryId"],
            CategoryName = reader["CategoryName"].ToString() ?? string.Empty,
            Description = reader["Description"] as string,
            IsDeleted = reader["IsDeleted"] != DBNull.Value && (bool)reader["IsDeleted"],
            CreatedAt = reader["CreatedAt"] == DBNull.Value ? DateTime.Now : (DateTime)reader["CreatedAt"]
        };
    }
}
