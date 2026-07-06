import React from "react";
import { useEffect, useState } from "react";
import { api } from "../services/api.js";

export default function StaffPage({ user, flash }) {
  const [tab, setTab] = useState("orders");
  const [data, setData] = useState({
    orders: [],
    support: [],
    notifications: [],
    report: {},
  });
  const [error, setError] = useState("");
  const isStaff = user?.role?.toLowerCase() === "staff";

  const load = async () => {
    if (!isStaff) return;
    const [orders, support, notifications, report] = await Promise.all([
      api.staffOrders(),
      api.staffSupport(),
      api.staffNotifications(),
      api.staffReport(),
    ]);
    setData({ orders, support, notifications, report });
  };

  useEffect(() => {
    load().catch((err) => setError(err.message));
  }, [isStaff]);

  if (!isStaff)
    return (
      <section>
        <h1>Staff</h1>
        <p>Please login with a staff account.</p>
      </section>
    );

  return (
    <section>
      <div className="section-title">
        <h1>Staff</h1>
        <span>Orders, support and notifications</span>
      </div>
      {error && <p className="error">{error}</p>}
      <div className="tabs">
        {["orders", "support", "notifications", "report"].map((item) => (
          <button
            key={item}
            className={tab === item ? "active" : ""}
            onClick={() => setTab(item)}
          >
            {item}
          </button>
        ))}
      </div>

      {tab === "orders" && (
        <DataTable
          rows={data.orders}
          columns={["orderId", "username", "totalAmount", "status", "paymentMethod"]}
          action={(row) => (
            <select
              value={row.status}
              onChange={async (e) => {
                await api.staffUpdateOrderStatus(row.orderId, e.target.value);
                await load();
                flash("Order status updated.");
              }}
            >
              <option>Pending</option>
              <option>Processing</option>
              <option>Shipping</option>
              <option>Completed</option>
              <option>Cancelled</option>
            </select>
          )}
        />
      )}

      {tab === "support" && (
        <DataTable
          rows={data.support}
          columns={["supportRequestId", "username", "subject", "message", "status"]}
          action={(row) => (
            <select
              value={row.status}
              disabled={row.status === "Closed"}
              onChange={async (e) => {
                try {
                  await api.staffUpdateSupportStatus(
                    row.supportRequestId,
                    e.target.value,
                  );
                  await load();
                  flash("Support request updated.");
                } catch (err) {
                  flash(err.message, "error");
                }
              }}
            >
              <option>Open</option>
              <option>InProgress</option>
              <option>Resolved</option>
              <option>Closed</option>
            </select>
          )}
        />
      )}

      {tab === "notifications" && (
        <DataTable
          rows={data.notifications}
          columns={["notificationId", "title", "message", "read", "createdAt"]}
        />
      )}

      {tab === "report" && (
        <div className="stats">
          <div className="panel">
            <span>Revenue</span>
            <strong>
              {Number(data.report.revenue || 0).toLocaleString()} đ
            </strong>
          </div>
          <div className="panel">
            <span>Orders</span>
            <strong>{data.report.orderCount || 0}</strong>
          </div>
        </div>
      )}
    </section>
  );
}

function DataTable({ rows, columns, action }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column}>{column}</th>
            ))}
            {action && <th></th>}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, index) => (
            <tr key={row.id || row[columns[0]] || index}>
              {columns.map((column) => (
                <td key={column}>{String(row[column] ?? "")}</td>
              ))}
              {action && <td>{action(row)}</td>}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
