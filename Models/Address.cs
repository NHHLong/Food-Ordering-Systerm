namespace FoodOrderWeb.Models;

public class Address
{
    public int AddressId { get; set; }
    public int UserId { get; set; }
    public string City { get; set; } = string.Empty;
    public string Street { get; set; } = string.Empty;
    public string BuildingNumber { get; set; } = string.Empty;
    public string FullAddress => $"{BuildingNumber} {Street}, {City}".Trim();
}
