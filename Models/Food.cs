namespace FoodOrderWeb.Models;

public class Food
{
    public int FoodId { get; set; }
    public string Name { get; set; } = string.Empty;
    public int? CategoryId { get; set; }
    public string CategoryName { get; set; } = string.Empty;
    public string? Description { get; set; }
    public decimal Price { get; set; }
    public string? Image { get; set; }
    public string Status { get; set; } = "Available";
    public bool IsDeleted { get; set; }
    public DateTime CreatedAt { get; set; }
}
