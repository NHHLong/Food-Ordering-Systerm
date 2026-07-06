const API_BASE = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options,
  });
  const text = await response.text();
  if (!response.ok) {
    let message = "Request failed.";
    try {
      const body = text ? JSON.parse(text) : null;
      message = body.message || message;
    } catch {
      message = text || message;
    }
    throw new Error(message);
  }
  if (response.status === 204) return null;
  return text ? JSON.parse(text) : null;
}

export const api = {
  login: (payload) =>
    request("/auth/login", { method: "POST", body: JSON.stringify(payload) }),
  register: (payload) =>
    request("/auth/register", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
  menu: (params = {}) => {
    const search = new URLSearchParams(
      Object.entries(params).filter(([, value]) => value),
    );
    return request(`/menu${search.size ? `?${search}` : ""}`);
  },
  foods: () => request("/foods"),
  cartItems: (userId) => request(`/cart?userId=${userId}`),
  addCartItem: (payload) =>
    request("/cart/items", { method: "POST", body: JSON.stringify(payload) }),
  updateCartItem: (payload) =>
    request("/cart/items", { method: "PUT", body: JSON.stringify(payload) }),
  removeCartItem: (userId, foodId) =>
    request(`/cart/items?userId=${userId}&foodId=${foodId}`, {
      method: "DELETE",
    }),
  clearCart: (userId) => request(`/cart?userId=${userId}`, { method: "DELETE" }),
  applyVoucher: (payload) =>
    request("/cart/voucher", { method: "POST", body: JSON.stringify(payload) }),
  checkout: (payload) =>
    request("/cart/checkout", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
  orders: (userId) => request(`/orders${userId ? `?userId=${userId}` : ""}`),
  orderDetails: (orderId, userId, role) =>
    request(
      `/orders/${orderId}/details?userId=${userId}${role ? `&role=${role}` : ""}`,
    ),
  reviews: () => request("/reviews"),
  addReview: (payload) =>
    request("/reviews", { method: "POST", body: JSON.stringify(payload) }),
  support: () => request("/support"),
  createSupport: (payload) =>
    request("/support", { method: "POST", body: JSON.stringify(payload) }),
  notifications: (userId) =>
    request(`/notifications${userId ? `?userId=${userId}` : ""}`),
  markNotificationRead: (id) =>
    request(`/notifications/${id}/read`, { method: "PATCH" }),
  staffOrders: () => request("/staff/orders"),
  staffUpdateOrderStatus: (id, status) =>
    request(`/staff/orders/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify({ status }),
    }),
  staffSupport: () => request("/staff/support"),
  staffUpdateSupportStatus: (id, status) =>
    request(`/staff/support/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify({ status }),
    }),
  staffNotifications: () => request("/staff/notifications"),
  staffReport: () => request("/staff/report"),
  adminDashboard: () => request("/admin/dashboard"),
  adminFoods: () => request("/admin/foods"),
  saveFood: (payload) =>
    request("/admin/foods", { method: "POST", body: JSON.stringify(payload) }),
  deleteFood: (id) => request(`/admin/foods/${id}`, { method: "DELETE" }),
  adminCategories: () => request("/admin/categories"),
  saveCategory: (payload) =>
    request("/admin/categories", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
  setCategoryActive: (id, active) =>
    request(`/admin/categories/${id}/active`, {
      method: "PATCH",
      body: JSON.stringify({ active }),
    }),
  adminUsers: () => request("/admin/users"),
  setUserActive: (id, active) =>
    request(`/admin/users/${id}/active`, {
      method: "PATCH",
      body: JSON.stringify({ active }),
    }),
  setUserRole: (id, role) =>
    request(`/admin/users/${id}/role`, {
      method: "PATCH",
      body: JSON.stringify({ role }),
    }),
  adminOrders: () => request("/admin/orders"),
  updateOrderStatus: (id, status) =>
    request(`/admin/orders/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify({ status }),
    }),
  adminVouchers: () => request("/admin/vouchers"),
  saveVoucher: (payload) =>
    request("/admin/vouchers", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
  adminSupport: () => request("/admin/support"),
  adminUpdateSupportStatus: (id, status) =>
    request(`/admin/support/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify({ status }),
    }),
};
