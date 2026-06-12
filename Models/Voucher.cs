namespace FoodOrderWeb.Models;

public class Voucher
{
    public int VoucherId { get; set; }
    public string Code { get; set; } = string.Empty;
    public string? Description { get; set; }
    public int? DiscountPercent { get; set; }
    public decimal? DiscountAmount { get; set; }
    public int MinimumOrderValue { get; set; }
    public int MaxUsage { get; set; }
    public int UsedCount { get; set; }
    public DateTime StartDate { get; set; }
    public DateTime ExpiryDate { get; set; }
    public bool IsActive { get; set; }
}
