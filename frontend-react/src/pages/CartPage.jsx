import React from "react";
import { useState } from "react";
import { api } from "../services/api.js";

export default function CartPage({
  user,
  cart,
  setCart,
  voucher,
  setVoucher,
  total,
  finalTotal,
  flash,
  setPage,
}) {
  const [code, setCode] = useState(voucher?.code || "");
  const [paymentMethod, setPaymentMethod] = useState("Cash");
  const [deliveryAddress, setDeliveryAddress] = useState({
    city: "",
    street: "",
    buildingNumber: "",
  });
  const [error, setError] = useState("");

  const update = async (foodId, quantity) => {
    const nextQuantity = Math.max(1, Number(quantity));
    if (user?.userId) {
      try {
        const items = await api.updateCartItem({
          userId: user.userId,
          foodId,
          quantity: nextQuantity,
        });
        setCart(items);
        return;
      } catch (err) {
        setError(err.message);
      }
    }
    setCart(cart.map((item) =>
      item.food.foodId === foodId
        ? { ...item, quantity: nextQuantity }
        : item,
    ));
  };

  const remove = async (foodId) => {
    if (user?.userId) {
      try {
        const items = await api.removeCartItem(user.userId, foodId);
        setCart(items);
        return;
      } catch (err) {
        setError(err.message);
      }
    }
    setCart(cart.filter((x) => x.food.foodId !== foodId));
  };

  const applyVoucher = async (event) => {
    event.preventDefault();
    setError("");
    try {
      const applied = await api.applyVoucher({ code, orderTotal: total });
      setVoucher(applied);
      flash("Voucher applied.");
    } catch (err) {
      setError(err.message);
    }
  };

  const checkout = async (event) => {
    event.preventDefault();
    if (!user) {
      setPage("login");
      return;
    }
    setError("");
    try {
      await api.checkout({
        user,
        items: cart,
        voucher,
        paymentMethod,
        shippingAddress: `${deliveryAddress.buildingNumber} ${deliveryAddress.street}, ${deliveryAddress.city}`.trim(),
        deliveryAddress,
      });
      setCart([]);
      setVoucher(null);
      flash("Order placed successfully.");
      setPage("orders");
    } catch (err) {
      setError(err.message);
    }
  };

  if (!cart.length)
    return (
      <section>
        <h1>Cart</h1>
        <p>Your cart is empty.</p>
      </section>
    );

  return (
    <section>
      <h1>Cart</h1>
      {error && <p className="error">{error}</p>}
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Food</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Total</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {cart.map((item) => (
              <tr key={item.food.foodId}>
                <td>{item.food.name}</td>
                <td>{Number(item.food.price).toLocaleString()} đ</td>
                <td>
                  <input
                    type="number"
                    min="1"
                    value={item.quantity}
                    onChange={(e) => update(item.food.foodId, e.target.value)}
                  />
                </td>
                <td>
                  {Number(item.food.price * item.quantity).toLocaleString()} đ
                </td>
                <td>
                  <button
                    className="danger"
                    onClick={() => remove(item.food.foodId)}
                  >
                    Remove
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <form className="filters compact" onSubmit={applyVoucher}>
        <input
          value={code}
          onChange={(e) => setCode(e.target.value)}
          placeholder="Voucher code"
        />
        <button>Apply</button>
      </form>
      <div className="totals">
        <span>
          Total: <strong>{total.toLocaleString()} đ</strong>
        </span>
        <span>
          Final total: <strong>{finalTotal.toLocaleString()} đ</strong>
        </span>
      </div>
      <form className="panel form-stack" onSubmit={checkout}>
        <h2>Checkout</h2>
        <label>
          City
          <input
            value={deliveryAddress.city}
            onChange={(e) =>
              setDeliveryAddress({ ...deliveryAddress, city: e.target.value })
            }
            placeholder="Ho Chi Minh City"
            required
          />
        </label>
        <label>
          Street
          <input
            value={deliveryAddress.street}
            onChange={(e) =>
              setDeliveryAddress({ ...deliveryAddress, street: e.target.value })
            }
            placeholder="Nguyen Trai"
            required
          />
        </label>
        <label>
          Building number
          <input
            value={deliveryAddress.buildingNumber}
            onChange={(e) =>
              setDeliveryAddress({
                ...deliveryAddress,
                buildingNumber: e.target.value,
              })
            }
            placeholder="12"
            required
          />
        </label>
        <label>
          Payment
          <select
            value={paymentMethod}
            onChange={(e) => setPaymentMethod(e.target.value)}
          >
            <option value="Cash">Cash</option>
            <option value="BankTransfer">Bank transfer</option>
          </select>
        </label>
        <button>Place order</button>
      </form>
    </section>
  );
}
