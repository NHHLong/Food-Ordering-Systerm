using FoodOrderWeb.BLL;
using FoodOrderWeb.Helpers;
using FoodOrderWeb.Models;
using Microsoft.AspNetCore.Mvc;

namespace FoodOrderWeb.Controllers;

public class OrdersController : Controller
{
    private readonly OrderService _orders;

    public OrdersController(OrderService orders)
    {
        _orders = orders;
    }

    public IActionResult History()
    {
        var user = HttpContext.Session.GetObject<User>("CurrentUser");
        if (user == null)
        {
            return RedirectToAction("Login", "Account");
        }

        var isAdmin = user.Role.Equals("Admin", StringComparison.OrdinalIgnoreCase);
        return View(_orders.GetOrders(isAdmin ? null : user.UserId));
    }

    public IActionResult Details(int id)
    {
        ViewBag.OrderId = id;
        return View(_orders.GetDetails(id));
    }
}
