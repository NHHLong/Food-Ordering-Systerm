namespace FoodOrderWeb.Models;

public class User
{
    public int UserId { get; set; }
    public string Username { get; set; } = string.Empty;
    public string Password { get; set; } = string.Empty;
    public string? Email { get; set; }
    public string? Phone { get; set; }
    public string Role { get; set; } = "Customer";
    public DateTime CreatedAt { get; set; }
    public bool IsActive { get; set; } = true;
}
