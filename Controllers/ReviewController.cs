using FoodOrderWeb.BLL;
using FoodOrderWeb.Helpers;
using FoodOrderWeb.Models;
using Microsoft.AspNetCore.Mvc;

namespace FoodOrderWeb.Controllers;

public class ReviewController : Controller
{
    private readonly ReviewService _reviews;
    private readonly FoodService _foods;

    public ReviewController(ReviewService reviews, FoodService foods)
    {
        _reviews = reviews;
        _foods = foods;
    }

    public IActionResult Index()
    {
        ViewBag.Foods = _foods.GetAll();
        return View(_reviews.GetAll());
    }

    [HttpPost]
    public IActionResult Add(int foodId, int rating, string? comment)
    {
        var user = HttpContext.Session.GetObject<User>("CurrentUser");
        if (user == null)
        {
            return RedirectToAction("Login", "Account");
        }

        _reviews.Add(new Review { UserId = user.UserId, FoodId = foodId, Rating = rating, Comment = comment });
        return RedirectToAction(nameof(Index));
    }
}
