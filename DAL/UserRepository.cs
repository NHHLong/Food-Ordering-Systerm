using FoodOrderWeb.Models;
using Microsoft.Data.SqlClient;

namespace FoodOrderWeb.DAL;

public class UserRepository
{
    private readonly DbContext _db;

    public UserRepository(DbContext db)
    {
        _db = db;
    }

    public User? Login(string username, string password)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
SELECT UserId, Username, Password, Email, Phone, Role, CreatedAt, IsActive
FROM Users
WHERE Username = @username AND Password = @password AND IsActive = 1";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@username", username);
        cmd.Parameters.AddWithValue("@password", password);

        using var reader = cmd.ExecuteReader();
        return reader.Read() ? Map(reader) : null;
    }

    public bool UsernameExists(string username)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        using var cmd = new SqlCommand("SELECT COUNT(1) FROM Users WHERE Username = @username", conn);
        cmd.Parameters.AddWithValue("@username", username);
        return (int)cmd.ExecuteScalar()! > 0;
    }

    public int Register(User user)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
INSERT INTO Users (Username, Password, Email, Phone, Role, CreatedAt, IsActive)
OUTPUT INSERTED.UserId
VALUES (@username, @password, @email, @phone, @role, GETDATE(), 1)";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@username", user.Username);
        cmd.Parameters.AddWithValue("@password", user.Password);
        cmd.Parameters.AddWithValue("@email", (object?)user.Email ?? DBNull.Value);
        cmd.Parameters.AddWithValue("@phone", (object?)user.Phone ?? DBNull.Value);
        cmd.Parameters.AddWithValue("@role", user.Role);
        return (int)cmd.ExecuteScalar()!;
    }

    public List<User> GetAll()
    {
        var users = new List<User>();
        using var conn = _db.GetConnection();
        conn.Open();

        using var cmd = new SqlCommand("SELECT UserId, Username, Password, Email, Phone, Role, CreatedAt, IsActive FROM Users ORDER BY UserId", conn);
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            users.Add(Map(reader));
        }

        return users;
    }

    public void SetActive(int userId, bool isActive)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        using var cmd = new SqlCommand("UPDATE Users SET IsActive = @isActive WHERE UserId = @userId", conn);
        cmd.Parameters.AddWithValue("@isActive", isActive);
        cmd.Parameters.AddWithValue("@userId", userId);
        cmd.ExecuteNonQuery();
    }

    private static User Map(SqlDataReader reader)
    {
        return new User
        {
            UserId = (int)reader["UserId"],
            Username = reader["Username"].ToString() ?? string.Empty,
            Password = reader["Password"].ToString() ?? string.Empty,
            Email = reader["Email"] as string,
            Phone = reader["Phone"] as string,
            Role = reader["Role"].ToString() ?? "Customer",
            CreatedAt = reader["CreatedAt"] == DBNull.Value ? DateTime.Now : (DateTime)reader["CreatedAt"],
            IsActive = reader["IsActive"] == DBNull.Value || (bool)reader["IsActive"]
        };
    }
}
