using FoodOrderWeb.Models;
using Microsoft.Data.SqlClient;

namespace FoodOrderWeb.DAL;

public class SupportRepository
{
    private readonly DbContext _db;

    public SupportRepository(DbContext db)
    {
        _db = db;
    }

    public List<SupportRequest> GetAll()
    {
        var requests = new List<SupportRequest>();
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
SELECT s.SupportRequestId, s.UserId, u.Username, s.Subject, s.Message, s.Status, s.CreatedAt
FROM SupportRequests s
JOIN Users u ON s.UserId = u.UserId
ORDER BY s.CreatedAt DESC";

        using var cmd = new SqlCommand(query, conn);
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            requests.Add(new SupportRequest
            {
                SupportRequestId = (int)reader["SupportRequestId"],
                UserId = (int)reader["UserId"],
                Username = reader["Username"].ToString() ?? string.Empty,
                Subject = reader["Subject"].ToString() ?? string.Empty,
                Message = reader["Message"].ToString() ?? string.Empty,
                Status = reader["Status"].ToString() ?? "Open",
                CreatedAt = (DateTime)reader["CreatedAt"]
            });
        }

        return requests;
    }

    public void Add(SupportRequest request)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
INSERT INTO SupportRequests (UserId, Subject, Message, Status, CreatedAt)
VALUES (@userId, @subject, @message, 'Open', GETDATE())";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@userId", request.UserId);
        cmd.Parameters.AddWithValue("@subject", request.Subject);
        cmd.Parameters.AddWithValue("@message", request.Message);
        cmd.ExecuteNonQuery();
    }
}
