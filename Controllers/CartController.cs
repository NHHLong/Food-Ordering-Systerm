using FoodOrderWeb.BLL;
using FoodOrderWeb.Helpers;
using FoodOrderWeb.Models;
using Microsoft.AspNetCore.Mvc;

namespace FoodOrderWeb.Controllers;

public class CartController : Controller
{
    private readonly CartService _cart;
    private readonly FoodService _foods;
    private readonly VoucherService _vouchers;
    private readonly OrderService _orders;

    public CartController(CartService cart, FoodService foods, VoucherService vouchers, OrderService orders)
    {
        _cart = cart;
        _foods = foods;
        _vouchers = vouchers;
        _orders = orders;
    }

    public IActionResult Index()
    {
        LoadTotals();
        return View(_cart.GetItems(HttpContext.Session));
    }

    [HttpPost]
    public IActionResult Add(int foodId)
    {
        var food = _foods.GetById(foodId);
        if (food != null)
        {
            _cart.Add(HttpContext.Session, food);
        }

        return RedirectToAction("Index", "Menu");
    }

    [HttpPost]
    public IActionResult Update(int foodId, int quantity)
    {
        _cart.UpdateQuantity(HttpContext.Session, foodId, quantity);
        return RedirectToAction(nameof(Index));
    }

    [HttpPost]
    public IActionResult Remove(int foodId)
    {
        _cart.Remove(HttpContext.Session, foodId);
        return RedirectToAction(nameof(Index));
    }

    [HttpPost]
    public IActionResult ApplyVoucher(string code)
    {
        try
        {
            var voucher = _vouchers.Apply(code, _cart.GetTotal(HttpContext.Session));
            _cart.ApplyVoucher(HttpContext.Session, voucher);
            TempData["Message"] = "Voucher applied.";
        }
        catch (Exception ex)
        {
            TempData["Error"] = ex.Message;
        }

        return RedirectToAction(nameof(Index));
    }

    [HttpPost]
    public IActionResult Checkout(PaymentMethod paymentMethod, string? shippingAddress)
    {
        var user = HttpContext.Session.GetObject<User>("CurrentUser");
        if (user == null)
        {
            return RedirectToAction("Login", "Account");
        }

        try
        {
            _orders.PlaceOrder(
                user,
                _cart.GetItems(HttpContext.Session),
                _cart.GetVoucher(HttpContext.Session),
                paymentMethod,
                shippingAddress);

            _cart.Clear(HttpContext.Session);
            TempData["Message"] = "Order placed successfully.";
            return RedirectToAction("History", "Orders");
        }
        catch (Exception ex)
        {
            TempData["Error"] = ex.Message;
            return RedirectToAction(nameof(Index));
        }
    }

    private void LoadTotals()
    {
        ViewBag.Total = _cart.GetTotal(HttpContext.Session);
        ViewBag.FinalTotal = _cart.GetFinalTotal(HttpContext.Session);
        ViewBag.Voucher = _cart.GetVoucher(HttpContext.Session);
    }
}
