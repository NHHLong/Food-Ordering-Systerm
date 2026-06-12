using FoodOrderWeb.BLL;
using FoodOrderWeb.Helpers;
using FoodOrderWeb.Models;
using Microsoft.AspNetCore.Mvc;

namespace FoodOrderWeb.Controllers;

public class SupportController : Controller
{
    private readonly SupportService _support;

    public SupportController(SupportService support)
    {
        _support = support;
    }

    public IActionResult Index() => View();

    [HttpPost]
    public IActionResult Create(string subject, string message)
    {
        var user = HttpContext.Session.GetObject<User>("CurrentUser");
        if (user == null)
        {
            return RedirectToAction("Login", "Account");
        }

        _support.Add(new SupportRequest { UserId = user.UserId, Subject = subject, Message = message });
        TempData["Message"] = "Support request sent.";
        return RedirectToAction(nameof(Index));
    }
}
