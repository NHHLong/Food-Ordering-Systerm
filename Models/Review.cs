namespace FoodOrderWeb.Models;

public class Review
{
    public int ReviewId { get; set; }
    public int UserId { get; set; }
    public int FoodId { get; set; }
    public string Username { get; set; } = string.Empty;
    public string FoodName { get; set; } = string.Empty;
    public int Rating { get; set; }
    public string? Comment { get; set; }
    public DateTime ReviewDate { get; set; }
}
