import React from "react";
import { useEffect, useState } from "react";
import { api } from "../services/api.js";

export default function ReviewsPage({ user, flash }) {
  const [reviews, setReviews] = useState([]);
  const [foods, setFoods] = useState([]);
  const [form, setForm] = useState({ foodId: "", rating: 5, comment: "" });
  const [error, setError] = useState("");

  const load = async () => {
    const [reviewRows, foodRows] = await Promise.all([
      api.reviews(),
      api.foods(),
    ]);
    setReviews(reviewRows);
    setFoods(foodRows);
  };

  useEffect(() => {
    load().catch((err) => setError(err.message));
  }, []);

  const submit = async (event) => {
    event.preventDefault();
    if (!user) {
      setError("Please login before reviewing.");
      return;
    }
    try {
      await api.addReview({
        userId: user.userId,
        foodId: Number(form.foodId),
        rating: Number(form.rating),
        comment: form.comment,
      });
      setForm({ foodId: "", rating: 5, comment: "" });
      await load();
      flash("Review submitted.");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <section>
      <h1>Reviews</h1>
      {error && <p className="error">{error}</p>}
      <form className="panel form-grid" onSubmit={submit}>
        <select
          required
          value={form.foodId}
          onChange={(e) => setForm({ ...form, foodId: e.target.value })}
        >
          <option value="">Choose food</option>
          {foods.map((food) => (
            <option key={food.foodId} value={food.foodId}>
              {food.name}
            </option>
          ))}
        </select>
        <input
          type="number"
          min="1"
          max="5"
          value={form.rating}
          onChange={(e) => setForm({ ...form, rating: e.target.value })}
        />
        <input
          value={form.comment}
          onChange={(e) => setForm({ ...form, comment: e.target.value })}
          placeholder="Comment"
        />
        <button>Review</button>
      </form>
      <div className="list">
        {reviews.map((review) => (
          <article className="panel" key={review.reviewId}>
            <strong>{review.foodName}</strong>
            <span>
              {"★".repeat(review.rating)}
              {"☆".repeat(5 - review.rating)}
            </span>
            <p>{review.comment}</p>
            <p className="muted">
              By {review.username} · {review.reviewDate?.replace("T", " ")}
            </p>
          </article>
        ))}
      </div>
    </section>
  );
}
