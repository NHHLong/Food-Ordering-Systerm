using FoodOrderWeb.BLL;
using FoodOrderWeb.Helpers;
using FoodOrderWeb.Models;
using Microsoft.AspNetCore.Mvc;

namespace FoodOrderWeb.Controllers;

public class AdminController : Controller
{
    private readonly FoodService _foods;
    private readonly CategoryService _categories;
    private readonly UserService _users;
    private readonly OrderService _orders;
    private readonly VoucherService _vouchers;
    private readonly SupportService _support;

    public AdminController(FoodService foods, CategoryService categories, UserService users, OrderService orders, VoucherService vouchers, SupportService support)
    {
        _foods = foods;
        _categories = categories;
        _users = users;
        _orders = orders;
        _vouchers = vouchers;
        _support = support;
    }

    public IActionResult Index()
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        ViewBag.Revenue = _orders.GetRevenue();
        ViewBag.OrderCount = _orders.GetOrders().Count;
        return View();
    }

    public IActionResult Foods()
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        ViewBag.Categories = _categories.GetAll();
        return View(_foods.GetAll());
    }

    [HttpPost]
    public IActionResult SaveFood(Food food)
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        _foods.Save(food);
        return RedirectToAction(nameof(Foods));
    }

    [HttpPost]
    public IActionResult DeleteFood(int foodId)
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        _foods.Delete(foodId);
        return RedirectToAction(nameof(Foods));
    }

    public IActionResult Categories()
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        return View(_categories.GetAll());
    }

    [HttpPost]
    public IActionResult SaveCategory(Category category)
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        _categories.Save(category);
        return RedirectToAction(nameof(Categories));
    }

    public IActionResult Users()
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        return View(_users.GetAll());
    }

    [HttpPost]
    public IActionResult SetUserActive(int userId, bool isActive)
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        _users.SetActive(userId, isActive);
        return RedirectToAction(nameof(Users));
    }

    public IActionResult Orders()
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        return View(_orders.GetOrders());
    }

    [HttpPost]
    public IActionResult UpdateOrderStatus(int orderId, string status)
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        _orders.UpdateStatus(orderId, status);
        return RedirectToAction(nameof(Orders));
    }

    public IActionResult Vouchers()
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        return View(_vouchers.GetAll());
    }

    [HttpPost]
    public IActionResult SaveVoucher(Voucher voucher)
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        _vouchers.Save(voucher);
        return RedirectToAction(nameof(Vouchers));
    }

    public IActionResult SupportRequests()
    {
        if (!IsAdmin()) return RedirectToAction("Login", "Account");
        return View(_support.GetAll());
    }

    private bool IsAdmin()
    {
        var user = HttpContext.Session.GetObject<User>("CurrentUser");
        return user?.Role.Equals("Admin", StringComparison.OrdinalIgnoreCase) == true;
    }
}
