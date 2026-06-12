using FoodOrderWeb.DAL;
using FoodOrderWeb.Models;

namespace FoodOrderWeb.BLL;

public class FoodService
{
    private readonly FoodRepository _foods;

    public FoodService(FoodRepository foods)
    {
        _foods = foods;
    }

    public List<Food> GetAll(string? keyword = null, int? categoryId = null) => _foods.GetAll(keyword, categoryId);

    public Food? GetById(int foodId) => _foods.GetById(foodId);

    public void Save(Food food)
    {
        if (string.IsNullOrWhiteSpace(food.Name))
        {
            throw new InvalidOperationException("Food name is required.");
        }

        if (food.Price <= 0)
        {
            throw new InvalidOperationException("Price must be greater than zero.");
        }

        _foods.Save(food);
    }

    public void Delete(int foodId) => _foods.Delete(foodId);
}
