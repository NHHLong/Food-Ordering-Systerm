import React from "react";
import { useEffect, useState } from "react";
import { api } from "../services/api.js";

const emptyFood = {
  foodId: 0,
  name: "",
  price: 0,
  status: "Available",
  image: "",
  categoryId: "",
  description: "",
};
const emptyCategory = {
  categoryId: 0,
  name: "",
  description: "",
  active: true,
};
const emptyVoucher = {
  voucherId: 0,
  code: "",
  description: "",
  discountPercent: "",
  discountAmount: "",
  minimumOrderValue: 0,
  maxUsage: 0,
  usedCount: 0,
  startDate: "",
  expiryDate: "",
  active: true,
};

export default function AdminPage({ user, flash }) {
  const [tab, setTab] = useState("dashboard");
  const [data, setData] = useState({
    dashboard: {},
    foods: [],
    categories: [],
    users: [],
    orders: [],
    vouchers: [],
    support: [],
  });
  const [food, setFood] = useState(emptyFood);
  const [category, setCategory] = useState(emptyCategory);
  const [voucher, setVoucher] = useState(emptyVoucher);
  const [error, setError] = useState("");

  const isAdmin = user?.role?.toLowerCase() === "admin";
  const load = async () => {
    if (!isAdmin) return;
    const [dashboard, foods, categories, users, orders, vouchers, support] =
      await Promise.all([
        api.adminDashboard(),
        api.adminFoods(),
        api.adminCategories(),
        api.adminUsers(),
        api.adminOrders(),
        api.adminVouchers(),
        api.adminSupport(),
      ]);
    setData({ dashboard, foods, categories, users, orders, vouchers, support });
  };

  useEffect(() => {
    load().catch((err) => setError(err.message));
  }, [isAdmin]);

  if (!isAdmin)
    return (
      <section>
        <h1>Admin</h1>
        <p>Please login with an admin account.</p>
      </section>
    );

  const saveFood = async (event) => {
    event.preventDefault();
    await api.saveFood({
      ...food,
      categoryId: food.categoryId ? Number(food.categoryId) : null,
      price: Number(food.price),
    });
    setFood(emptyFood);
    await load();
    flash("Food saved.");
  };
  const saveCategory = async (event) => {
    event.preventDefault();
    await api.saveCategory(category);
    setCategory(emptyCategory);
    await load();
    flash("Category saved.");
  };
  const saveVoucher = async (event) => {
    event.preventDefault();
    await api.saveVoucher({
      ...voucher,
      discountPercent:
        voucher.discountPercent === "" ? null : Number(voucher.discountPercent),
      discountAmount:
        voucher.discountAmount === "" ? null : Number(voucher.discountAmount),
      minimumOrderValue: Number(voucher.minimumOrderValue),
      maxUsage: Number(voucher.maxUsage),
      usedCount: Number(voucher.usedCount),
      startDate: voucher.startDate,
      expiryDate: voucher.expiryDate,
    });
    setVoucher(emptyVoucher);
    await load();
    flash("Voucher saved.");
  };

  return (
    <section>
      <div className="section-title">
        <h1>Admin</h1>
        <span>Manage system</span>
      </div>
      {error && <p className="error">{error}</p>}
      <div className="tabs">
        {[
          "dashboard",
          "foods",
          "categories",
          "users",
          "orders",
          "vouchers",
          "support",
        ].map((item) => (
          <button
            key={item}
            className={tab === item ? "active" : ""}
            onClick={() => setTab(item)}
          >
            {item}
          </button>
        ))}
      </div>

      {tab === "dashboard" && (
        <div className="stats">
          <div className="panel">
            <span>Revenue</span>
            <strong>
              {Number(data.dashboard.revenue || 0).toLocaleString()} đ
            </strong>
          </div>
          <div className="panel">
            <span>Orders</span>
            <strong>{data.dashboard.orderCount || 0}</strong>
          </div>
        </div>
      )}

      {tab === "foods" && (
        <>
          <form className="panel form-grid" onSubmit={saveFood}>
            <input
              placeholder="Name"
              value={food.name}
              onChange={(e) => setFood({ ...food, name: e.target.value })}
            />
            <input
              type="number"
              placeholder="Price"
              value={food.price}
              onChange={(e) => setFood({ ...food, price: e.target.value })}
            />
            <select
              value={food.categoryId}
              onChange={(e) => setFood({ ...food, categoryId: e.target.value })}
            >
              <option value="">No category</option>
              {data.categories.map((x) => (
                <option key={x.categoryId} value={x.categoryId}>
                  {x.name}
                </option>
              ))}
            </select>
            <input
              placeholder="Image"
              value={food.image}
              onChange={(e) => setFood({ ...food, image: e.target.value })}
            />
            <input
              placeholder="Description"
              value={food.description}
              onChange={(e) =>
                setFood({ ...food, description: e.target.value })
              }
            />
            <button>Save food</button>
          </form>
          <DataTable
            rows={data.foods}
            columns={["foodId", "name", "categoryName", "price", "status"]}
            action={(row) => (
              <button
                className="danger"
                onClick={async () => {
                  await api.deleteFood(row.foodId);
                  await load();
                }}
              >
                Delete
              </button>
            )}
          />
        </>
      )}

      {tab === "categories" && (
        <>
          <form className="panel form-grid" onSubmit={saveCategory}>
            <input
              placeholder="Name"
              value={category.name}
              onChange={(e) =>
                setCategory({ ...category, name: e.target.value })
              }
            />
            <input
              placeholder="Description"
              value={category.description}
              onChange={(e) =>
                setCategory({ ...category, description: e.target.value })
              }
            />
            <label className="check">
              <input
                type="checkbox"
                checked={category.active}
                onChange={(e) =>
                  setCategory({ ...category, active: e.target.checked })
                }
              />{" "}
              Active
            </label>
            <button>Save category</button>
          </form>
          <DataTable
            rows={data.categories}
            columns={["categoryId", "name", "description", "active"]}
          />
        </>
      )}

      {tab === "users" && (
        <DataTable
          rows={data.users}
          columns={["userId", "username", "email", "phone", "role", "active"]}
          action={(row) => (
            <div className="row-actions">
              <select
                value={row.role}
                onChange={async (e) => {
                  await api.setUserRole(row.userId, e.target.value);
                  await load();
                }}
              >
                <option>Customer</option>
                <option>Staff</option>
                <option>Admin</option>
              </select>
              <button
                onClick={async () => {
                  await api.setUserActive(row.userId, !row.active);
                  await load();
                }}
              >
                {row.active ? "Deactivate" : "Activate"}
              </button>
            </div>
          )}
        />
      )}

      {tab === "orders" && (
        <DataTable
          rows={data.orders}
          columns={[
            "orderId",
            "username",
            "totalAmount",
            "status",
            "paymentMethod",
          ]}
          action={(row) => (
            <select
              value={row.status}
              onChange={async (e) => {
                await api.updateOrderStatus(row.orderId, e.target.value);
                await load();
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

      {tab === "vouchers" && (
        <>
          <form className="panel form-grid" onSubmit={saveVoucher}>
            <input
              placeholder="Code"
              value={voucher.code}
              onChange={(e) => setVoucher({ ...voucher, code: e.target.value })}
            />
            <input
              placeholder="Description"
              value={voucher.description}
              onChange={(e) =>
                setVoucher({ ...voucher, description: e.target.value })
              }
            />
            <input
              type="number"
              placeholder="Percent"
              value={voucher.discountPercent}
              onChange={(e) =>
                setVoucher({ ...voucher, discountPercent: e.target.value })
              }
            />
            <input
              type="number"
              placeholder="Amount"
              value={voucher.discountAmount}
              onChange={(e) =>
                setVoucher({ ...voucher, discountAmount: e.target.value })
              }
            />
            <input
              type="number"
              placeholder="Minimum"
              value={voucher.minimumOrderValue}
              onChange={(e) =>
                setVoucher({ ...voucher, minimumOrderValue: e.target.value })
              }
            />
            <input
              type="number"
              placeholder="Max usage"
              value={voucher.maxUsage}
              onChange={(e) =>
                setVoucher({ ...voucher, maxUsage: e.target.value })
              }
            />
            <input
              type="datetime-local"
              value={voucher.startDate}
              onChange={(e) =>
                setVoucher({ ...voucher, startDate: e.target.value })
              }
            />
            <input
              type="datetime-local"
              value={voucher.expiryDate}
              onChange={(e) =>
                setVoucher({ ...voucher, expiryDate: e.target.value })
              }
            />
            <button>Save voucher</button>
          </form>
          <DataTable
            rows={data.vouchers}
            columns={[
              "voucherId",
              "code",
              "discountPercent",
              "discountAmount",
              "usedCount",
              "active",
            ]}
          />
        </>
      )}

      {tab === "support" && (
        <DataTable
          rows={data.support}
          columns={[
            "supportRequestId",
            "username",
            "subject",
            "message",
            "status",
          ]}
        />
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
