using FoodOrderWeb.BLL;
using FoodOrderWeb.Helpers;
using FoodOrderWeb.Models;
using FoodOrderWeb.Models.ViewModels;
using Microsoft.AspNetCore.Mvc;

namespace FoodOrderWeb.Controllers;

public class AccountController : Controller
{
    private readonly UserService _users;

    public AccountController(UserService users)
    {
        _users = users;
    }

    public IActionResult Login() => View(new LoginViewModel());

    [HttpPost]
    public IActionResult Login(LoginViewModel model)
    {
        if (!ModelState.IsValid)
        {
            return View(model);
        }

        try
        {
            var user = _users.Login(model.Username, model.Password);
            if (user == null)
            {
                ModelState.AddModelError("", "Wrong username or password.");
                return View(model);
            }

            HttpContext.Session.SetObject("CurrentUser", user);
            return RedirectToAction("Index", "Menu");
        }
        catch (Exception ex)
        {
            ModelState.AddModelError("", ex.Message);
            return View(model);
        }
    }

    public IActionResult Register() => View(new RegisterViewModel());

    [HttpPost]
    public IActionResult Register(RegisterViewModel model)
    {
        if (!ModelState.IsValid)
        {
            return View(model);
        }

        try
        {
            _users.Register(new User
            {
                Username = model.Username,
                Password = model.Password,
                Email = model.Email,
                Phone = model.Phone,
                Role = "Customer"
            });

            TempData["Message"] = "Register success. Please login.";
            return RedirectToAction(nameof(Login));
        }
        catch (Exception ex)
        {
            ModelState.AddModelError("", ex.Message);
            return View(model);
        }
    }

    public IActionResult Logout()
    {
        HttpContext.Session.Clear();
        return RedirectToAction(nameof(Login));
    }
}
