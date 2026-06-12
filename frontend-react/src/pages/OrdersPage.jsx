import React from "react";
import { useEffect, useState } from "react";
import { api } from "../services/api.js";

export default function OrdersPage({ user }) {
  const [orders, setOrders] = useState([]);
  const [details, setDetails] = useState({});
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user) return;
    const admin = user.role?.toLowerCase() === "admin";
    api
      .orders(admin ? null : user.userId)
      .then(setOrders)
      .catch((err) => setError(err.message));
  }, [user]);

  const toggleDetails = async (orderId) => {
    if (details[orderId]) {
      setDetails({ ...details, [orderId]: null });
      return;
    }
    const rows = await api.orderDetails(orderId);
    setDetails({ ...details, [orderId]: rows });
  };

  if (!user)
    return (
      <section>
        <h1>Orders</h1>
        <p>Please login before viewing orders.</p>
      </section>
    );

  return (
    <section>
      <h1>Order History</h1>
      {error && <p className="error">{error}</p>}
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Id</th>
              <th>User</th>
              <th>Date</th>
              <th>Total</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.orderId}>
                <td>#{order.orderId}</td>
                <td>{order.username}</td>
                <td>{order.orderDate?.replace("T", " ")}</td>
                <td>{Number(order.totalAmount).toLocaleString()} đ</td>
                <td>{order.status}</td>
                <td>
                  <button onClick={() => toggleDetails(order.orderId)}>
                    Details
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {Object.entries(details).map(
        ([orderId, rows]) =>
          rows && (
            <div className="panel" key={orderId}>
              <h2>Order #{orderId}</h2>
              {rows.map((item) => (
                <p key={item.id}>
                  {item.foodName} x {item.quantity} -{" "}
                  {Number(item.totalPrice).toLocaleString()} đ
                </p>
              ))}
            </div>
          ),
      )}
    </section>
  );
}
