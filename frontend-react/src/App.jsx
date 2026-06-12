import React from "react";
import { useEffect, useMemo, useState } from "react";
import NavBar from "./components/NavBar.jsx";
import Notice from "./components/Notice.jsx";
import AdminPage from "./pages/AdminPage.jsx";
import AuthPage from "./pages/AuthPage.jsx";
import CartPage from "./pages/CartPage.jsx";
import MenuPage from "./pages/MenuPage.jsx";
import NotificationsPage from "./pages/NotificationsPage.jsx";
import OrdersPage from "./pages/OrdersPage.jsx";
import ReviewsPage from "./pages/ReviewsPage.jsx";
import StaffPage from "./pages/StaffPage.jsx";
import SupportPage from "./pages/SupportPage.jsx";
import { api } from "./services/api.js";

const readStorage = (key, fallback) => {
  try {
    return JSON.parse(localStorage.getItem(key)) ?? fallback;
  } catch {
    return fallback;
  }
};

export default function App() {
  const [page, setPage] = useState("menu");
  const [user, setUser] = useState(() => readStorage("foodorder:user", null));
  const [cart, setCart] = useState(() => readStorage("foodorder:cart", []));
  const [voucher, setVoucher] = useState(() =>
    readStorage("foodorder:voucher", null),
  );
  const [notice, setNotice] = useState(null);

  useEffect(
    () => localStorage.setItem("foodorder:user", JSON.stringify(user)),
    [user],
  );
  useEffect(
    () => localStorage.setItem("foodorder:cart", JSON.stringify(cart)),
    [cart],
  );
  useEffect(
    () => localStorage.setItem("foodorder:voucher", JSON.stringify(voucher)),
    [voucher],
  );
  useEffect(() => {
    if (!user?.userId) return;
    api
      .cartItems(user.userId)
      .then(setCart)
      .catch(() => setCart(readStorage("foodorder:cart", [])));
  }, [user?.userId]);

  const total = useMemo(
    () => cart.reduce((sum, item) => sum + item.food.price * item.quantity, 0),
    [cart],
  );
  const finalTotal = useMemo(() => {
    if (!voucher) return total;
    let value = total;
    if (voucher.discountPercent)
      value -= (value * voucher.discountPercent) / 100;
    if (voucher.discountAmount) value -= voucher.discountAmount;
    return Math.max(0, value);
  }, [total, voucher]);

  const flash = (message, type = "success") => setNotice({ message, type });
  const addToCart = async (food) => {
    if (user?.userId) {
      try {
        const items = await api.addCartItem({
          userId: user.userId,
          foodId: food.foodId,
          quantity: 1,
        });
        setCart(items);
        flash("Food added to cart.");
        return;
      } catch (err) {
        flash(err.message, "error");
      }
    }
    setCart((items) => {
      const found = items.find((item) => item.food.foodId === food.foodId);
      if (found) {
        return items.map((item) =>
          item.food.foodId === food.foodId
            ? { ...item, quantity: item.quantity + 1 }
            : item,
        );
      }
      return [...items, { food, quantity: 1 }];
    });
    flash("Food added to cart.");
  };
  const logout = () => {
    setUser(null);
    setPage("login");
    flash("Logged out.");
  };

  const shared = {
    user,
    setUser,
    cart,
    setCart,
    voucher,
    setVoucher,
    total,
    finalTotal,
    flash,
    setPage,
  };
  const pages = {
    menu: <MenuPage addToCart={addToCart} />,
    cart: <CartPage {...shared} />,
    orders: <OrdersPage user={user} />,
    notifications: <NotificationsPage user={user} flash={flash} />,
    reviews: <ReviewsPage user={user} flash={flash} />,
    support: <SupportPage user={user} flash={flash} setPage={setPage} />,
    login: (
      <AuthPage
        mode="login"
        setUser={setUser}
        setPage={setPage}
        flash={flash}
      />
    ),
    register: (
      <AuthPage
        mode="register"
        setUser={setUser}
        setPage={setPage}
        flash={flash}
      />
    ),
    admin: <AdminPage user={user} flash={flash} />,
    staff: <StaffPage user={user} flash={flash} />,
  };

  return (
    <>
      <NavBar page={page} setPage={setPage} user={user} logout={logout} />
      <main className="shell">
        <Notice
          message={notice?.message}
          type={notice?.type}
          onClose={() => setNotice(null)}
        />
        {pages[page] || pages.menu}
      </main>
      <footer>© 2026 - FoodOrder</footer>
    </>
  );
}
