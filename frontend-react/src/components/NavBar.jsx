import React from "react";

const items = [
  ["menu", "Menu"],
  ["cart", "Cart"],
  ["orders", "Orders"],
  ["notifications", "Notifications"],
  ["reviews", "Reviews"],
  ["support", "Support"],
];

export default function NavBar({ page, setPage, user, logout, cartCount = 0 }) {
  const initial = user?.username?.[0]?.toUpperCase() || "?";
  return (
    <header className="topbar">
      <button className="brand" onClick={() => setPage("menu")}>
        <span className="brand-mark" aria-hidden="true">
          ◍
        </span>
        FoodOrder
      </button>
      <nav>
        {items.map(([key, label]) => (
          <button
            key={key}
            className={page === key ? "active" : ""}
            onClick={() => setPage(key)}
          >
            {label}
            {key === "cart" && cartCount > 0 && (
              <span className="cart-count">{cartCount}</span>
            )}
          </button>
        ))}
      </nav>
      <div className="account">
        {!user ? (
          <>
            <button onClick={() => setPage("login")}>Login</button>
            <button onClick={() => setPage("register")}>Register</button>
          </>
        ) : (
          <>
            {user.role?.toLowerCase() === "admin" && (
              <button onClick={() => setPage("admin")}>Admin</button>
            )}
            {user.role?.toLowerCase() === "staff" && (
              <button onClick={() => setPage("staff")}>Staff</button>
            )}
            <span className="user-chip">
              <span className="user-avatar">{initial}</span>
              {user.username}
            </span>
            <button onClick={logout}>Logout</button>
          </>
        )}
      </div>
    </header>
  );
}
