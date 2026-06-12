using FoodOrderWeb.Models;
using Microsoft.Data.SqlClient;

namespace FoodOrderWeb.DAL;

public class ReviewRepository
{
    private readonly DbContext _db;

    public ReviewRepository(DbContext db)
    {
        _db = db;
    }

    public List<Review> GetAll()
    {
        var reviews = new List<Review>();
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
SELECT r.ReviewId, r.UserId, r.FoodId, u.Username, f.Name AS FoodName,
       r.Rating, r.Comment, r.ReviewDate
FROM Reviews r
JOIN Users u ON r.UserId = u.UserId
JOIN Foods f ON r.FoodId = f.FoodId
ORDER BY r.ReviewDate DESC";

        using var cmd = new SqlCommand(query, conn);
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            reviews.Add(new Review
            {
                ReviewId = (int)reader["ReviewId"],
                UserId = (int)reader["UserId"],
                FoodId = (int)reader["FoodId"],
                Username = reader["Username"].ToString() ?? string.Empty,
                FoodName = reader["FoodName"].ToString() ?? string.Empty,
                Rating = (int)reader["Rating"],
                Comment = reader["Comment"] as string,
                ReviewDate = (DateTime)reader["ReviewDate"]
            });
        }

        return reviews;
    }

    public void Add(Review review)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
INSERT INTO Reviews (UserId, FoodId, Rating, Comment, ReviewDate)
VALUES (@userId, @foodId, @rating, @comment, GETDATE())";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@userId", review.UserId);
        cmd.Parameters.AddWithValue("@foodId", review.FoodId);
        cmd.Parameters.AddWithValue("@rating", review.Rating);
        cmd.Parameters.AddWithValue("@comment", (object?)review.Comment ?? DBNull.Value);
        cmd.ExecuteNonQuery();
    }
}
