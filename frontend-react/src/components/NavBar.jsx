import React from "react";

const items = [
  ["menu", "Menu"],
  ["cart", "Cart"],
  ["orders", "Orders"],
  ["notifications", "Notifications"],
  ["reviews", "Reviews"],
  ["support", "Support"],
];

export default function NavBar({ page, setPage, user, logout }) {
  return (
    <header className="topbar">
      <button className="brand" onClick={() => setPage("menu")}>
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
            <span>{user.username}</span>
            <button onClick={logout}>Logout</button>
          </>
        )}
      </div>
    </header>
  );
}
