using FoodOrderWeb.Models;
using Microsoft.Data.SqlClient;

namespace FoodOrderWeb.DAL;

public class CategoryRepository
{
    private readonly DbContext _db;

    public CategoryRepository(DbContext db)
    {
        _db = db;
    }

    public List<Category> GetAll(bool onlyActive = false)
    {
        var categories = new List<Category>();
        using var conn = _db.GetConnection();
        conn.Open();

        var query = "SELECT CategoryId, Name, Description, IsActive FROM Categories";
        if (onlyActive)
        {
            query += " WHERE IsActive = 1";
        }

        query += " ORDER BY Name";
        using var cmd = new SqlCommand(query, conn);
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            categories.Add(Map(reader));
        }

        return categories;
    }

    public void Save(Category category)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        var query = category.CategoryId == 0
            ? "INSERT INTO Categories (Name, Description, IsActive) VALUES (@name, @description, @isActive)"
            : "UPDATE Categories SET Name = @name, Description = @description, IsActive = @isActive WHERE CategoryId = @id";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@id", category.CategoryId);
        cmd.Parameters.AddWithValue("@name", category.Name);
        cmd.Parameters.AddWithValue("@description", (object?)category.Description ?? DBNull.Value);
        cmd.Parameters.AddWithValue("@isActive", category.IsActive);
        cmd.ExecuteNonQuery();
    }

    private static Category Map(SqlDataReader reader)
    {
        return new Category
        {
            CategoryId = (int)reader["CategoryId"],
            Name = reader["Name"].ToString() ?? string.Empty,
            Description = reader["Description"] as string,
            IsActive = reader["IsActive"] == DBNull.Value || (bool)reader["IsActive"]
        };
    }
}
