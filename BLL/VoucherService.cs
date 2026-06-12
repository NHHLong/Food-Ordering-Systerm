using FoodOrderWeb.DAL;
using FoodOrderWeb.Models;

namespace FoodOrderWeb.BLL;

public class VoucherService
{
    private readonly VoucherRepository _vouchers;

    public VoucherService(VoucherRepository vouchers)
    {
        _vouchers = vouchers;
    }

    public Voucher Apply(string code, decimal orderTotal)
    {
        var voucher = _vouchers.GetByCode(code.Trim());
        if (voucher == null)
        {
            throw new InvalidOperationException("Voucher does not exist.");
        }

        if (!voucher.IsActive)
        {
            throw new InvalidOperationException("Voucher is inactive.");
        }

        if (voucher.StartDate > DateTime.Now || voucher.ExpiryDate < DateTime.Now)
        {
            throw new InvalidOperationException("Voucher is expired or not started.");
        }

        if (orderTotal < voucher.MinimumOrderValue)
        {
            throw new InvalidOperationException("Order total does not meet voucher minimum value.");
        }

        if (voucher.MaxUsage > 0 && voucher.UsedCount >= voucher.MaxUsage)
        {
            throw new InvalidOperationException("Voucher usage limit reached.");
        }

        return voucher;
    }

    public List<Voucher> GetAll() => _vouchers.GetAll();

    public void Save(Voucher voucher) => _vouchers.Save(voucher);

    public void MarkUsed(int voucherId) => _vouchers.MarkUsed(voucherId);
}
