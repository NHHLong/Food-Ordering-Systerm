using FoodOrderWeb.Helpers;
using FoodOrderWeb.Models;

namespace FoodOrderWeb.BLL;

public class CartService
{
    private const string CartKey = "CartItems";
    private const string VoucherKey = "AppliedVoucher";
    private readonly IHttpContextAccessor? _accessor;

    public CartService(IHttpContextAccessor? accessor = null)
    {
        _accessor = accessor;
    }

    public List<CartItem> GetItems(ISession session)
    {
        return session.GetObject<List<CartItem>>(CartKey) ?? new List<CartItem>();
    }

    public void Add(ISession session, Food food)
    {
        var items = GetItems(session);
        var item = items.FirstOrDefault(x => x.Food.FoodId == food.FoodId);
        if (item == null)
        {
            items.Add(new CartItem { Food = food, Quantity = 1 });
        }
        else
        {
            item.Quantity++;
        }

        Save(session, items);
    }

    public void UpdateQuantity(ISession session, int foodId, int quantity)
    {
        var items = GetItems(session);
        var item = items.FirstOrDefault(x => x.Food.FoodId == foodId);
        if (item != null)
        {
            item.Quantity = Math.Max(1, quantity);
        }

        Save(session, items);
    }

    public void Remove(ISession session, int foodId)
    {
        var items = GetItems(session);
        items.RemoveAll(x => x.Food.FoodId == foodId);
        Save(session, items);
    }

    public decimal GetTotal(ISession session) => GetItems(session).Sum(x => x.TotalPrice);

    public Voucher? GetVoucher(ISession session) => session.GetObject<Voucher>(VoucherKey);

    public void ApplyVoucher(ISession session, Voucher voucher) => session.SetObject(VoucherKey, voucher);

    public decimal GetFinalTotal(ISession session)
    {
        var total = GetTotal(session);
        var voucher = GetVoucher(session);
        if (voucher == null)
        {
            return total;
        }

        if (voucher.DiscountPercent.HasValue)
        {
            total -= total * voucher.DiscountPercent.Value / 100;
        }

        if (voucher.DiscountAmount.HasValue)
        {
            total -= voucher.DiscountAmount.Value;
        }

        return Math.Max(0, total);
    }

    public void Clear(ISession session)
    {
        session.Remove(CartKey);
        session.Remove(VoucherKey);
    }

    private static void Save(ISession session, List<CartItem> items)
    {
        session.SetObject(CartKey, items);
    }
}
