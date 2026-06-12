import React from "react";
import { useEffect, useState } from "react";
import { api } from "../services/api.js";

export default function MenuPage({ addToCart }) {
  const [foods, setFoods] = useState([]);
  const [categories, setCategories] = useState([]);
  const [keyword, setKeyword] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const load = async (params = {}) => {
    setLoading(true);
    setError("");
    try {
      const data = await api.menu(params);
      setFoods(data.foods || []);
      setCategories(data.categories || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const submit = (event) => {
    event.preventDefault();
    load({ keyword, categoryId });
  };

  return (
    <section>
      <div className="section-title">
        <h1>Menu</h1>
        <span>{foods.length} foods</span>
      </div>
      <form className="filters" onSubmit={submit}>
        <input
          value={keyword}
          onChange={(event) => setKeyword(event.target.value)}
          placeholder="Search food"
        />
        <select
          value={categoryId}
          onChange={(event) => setCategoryId(event.target.value)}
        >
          <option value="">All categories</option>
          {categories.map((category) => (
            <option key={category.categoryId} value={category.categoryId}>
              {category.name}
            </option>
          ))}
        </select>
        <button>Search</button>
      </form>
      {loading && <p>Loading menu...</p>}
      {error && <p className="error">{error}</p>}
      <div className="food-grid">
        {foods.map((food) => (
          <article className="food-card" key={food.foodId}>
            {food.image && <img src={food.image} alt={food.name} />}
            <div>
              <h3>{food.name}</h3>
              <p className="muted">{food.categoryName}</p>
              <p>{food.description}</p>
              <div className="card-actions">
                <strong>{Number(food.price).toLocaleString()} đ</strong>
                <button onClick={() => addToCart(food)}>Add</button>
              </div>
            </div>
          </article>
        ))}
      </div>
    </section>
  );
}
