namespace FoodOrderWeb.Models;

public class Order
{
    public int OrderId { get; set; }
    public int UserId { get; set; }
    public string Username { get; set; } = string.Empty;
    public int? VoucherId { get; set; }
    public int? AddressId { get; set; }
    public DateTime OrderDate { get; set; }
    public decimal TotalAmount { get; set; }
    public string Status { get; set; } = "Pending";
    public PaymentMethod PaymentMethod { get; set; }
    public string? ShippingAddress { get; set; }
    public List<OrderDetail> Details { get; set; } = new();
}
