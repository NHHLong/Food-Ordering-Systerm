using FoodOrderWeb.BLL;
using Microsoft.AspNetCore.Mvc;

namespace FoodOrderWeb.Controllers;

public class MenuController : Controller
{
    private readonly FoodService _foods;
    private readonly CategoryService _categories;

    public MenuController(FoodService foods, CategoryService categories)
    {
        _foods = foods;
        _categories = categories;
    }

    public IActionResult Index(string? keyword, int? categoryId)
    {
        ViewBag.Categories = _categories.GetAll(true);
        ViewBag.Keyword = keyword;
        ViewBag.CategoryId = categoryId;
        return View(_foods.GetAll(keyword, categoryId));
    }
}
