using FoodOrderWeb.DAL;
using FoodOrderWeb.Models;

namespace FoodOrderWeb.BLL;

public class OrderService
{
    private readonly OrderRepository _orders;
    private readonly VoucherService _vouchers;

    public OrderService(OrderRepository orders, VoucherService vouchers)
    {
        _orders = orders;
        _vouchers = vouchers;
    }

    public int PlaceOrder(User user, List<CartItem> items, Voucher? voucher, PaymentMethod paymentMethod, string? shippingAddress)
    {
        if (user.UserId <= 0)
        {
            throw new InvalidOperationException("Please login before checkout.");
        }

        if (!items.Any())
        {
            throw new InvalidOperationException("Cart is empty.");
        }

        var total = items.Sum(x => x.TotalPrice);
        if (voucher != null)
        {
            if (voucher.DiscountPercent.HasValue)
            {
                total -= total * voucher.DiscountPercent.Value / 100;
            }

            if (voucher.DiscountAmount.HasValue)
            {
                total -= voucher.DiscountAmount.Value;
            }

            total = Math.Max(0, total);
        }

        var order = new Order
        {
            UserId = user.UserId,
            VoucherId = voucher?.VoucherId,
            TotalAmount = total,
            Status = "Pending",
            PaymentMethod = paymentMethod,
            ShippingAddress = shippingAddress,
            Details = items.Select(item => new OrderDetail
            {
                FoodId = item.Food.FoodId,
                Quantity = item.Quantity,
                UnitPrice = item.Food.Price
            }).ToList()
        };

        var orderId = _orders.CreateOrder(order);
        if (voucher != null)
        {
            _vouchers.MarkUsed(voucher.VoucherId);
        }

        return orderId;
    }

    public List<Order> GetOrders(int? userId = null) => _orders.GetOrders(userId);

    public List<OrderDetail> GetDetails(int orderId) => _orders.GetDetails(orderId);

    public void UpdateStatus(int orderId, string status) => _orders.UpdateStatus(orderId, status);

    public decimal GetRevenue() => _orders.GetRevenue();
}
