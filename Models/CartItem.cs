namespace FoodOrderWeb.Models;

public class CartItem
{
    public Food Food { get; set; } = new();
    public int Quantity { get; set; }
    public decimal TotalPrice => Food.Price * Quantity;
}
