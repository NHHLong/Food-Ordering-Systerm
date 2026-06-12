using FoodOrderWeb.DAL;
using FoodOrderWeb.Models;

namespace FoodOrderWeb.BLL;

public class CategoryService
{
    private readonly CategoryRepository _categories;

    public CategoryService(CategoryRepository categories)
    {
        _categories = categories;
    }

    public List<Category> GetAll(bool onlyActive = false) => _categories.GetAll(onlyActive);

    public void Save(Category category)
    {
        if (string.IsNullOrWhiteSpace(category.Name))
        {
            throw new InvalidOperationException("Category name is required.");
        }

        _categories.Save(category);
    }
}
