using FoodOrderWeb.DAL;
using FoodOrderWeb.Models;

namespace FoodOrderWeb.BLL;

public class ReviewService
{
    private readonly ReviewRepository _reviews;

    public ReviewService(ReviewRepository reviews)
    {
        _reviews = reviews;
    }

    public List<Review> GetAll() => _reviews.GetAll();

    public void Add(Review review)
    {
        if (review.Rating < 1 || review.Rating > 5)
        {
            throw new InvalidOperationException("Rating must be from 1 to 5.");
        }

        _reviews.Add(review);
    }
}
