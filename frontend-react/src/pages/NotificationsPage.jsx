import React from "react";
import { useEffect, useState } from "react";
import { api } from "../services/api.js";

export default function NotificationsPage({ user, flash }) {
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");

  const load = async () => {
    if (!user?.userId) return;
    setItems(await api.notifications(user.userId));
  };

  useEffect(() => {
    load().catch((err) => setError(err.message));
  }, [user?.userId]);

  if (!user)
    return (
      <section>
        <h1>Notifications</h1>
        <p>Please login to view notifications.</p>
      </section>
    );

  return (
    <section>
      <div className="section-title">
        <h1>Notifications</h1>
        <span>Order and support updates</span>
      </div>
      {error && <p className="error">{error}</p>}
      <div className="stack">
        {items.map((item) => (
          <article className="panel" key={item.notificationId}>
            <h2>{item.title}</h2>
            <p>{item.message}</p>
            <small>{item.createdAt ? new Date(item.createdAt).toLocaleString() : ""}</small>
            {!item.read && (
              <button
                onClick={async () => {
                  await api.markNotificationRead(item.notificationId);
                  await load();
                  flash("Notification marked as read.");
                }}
              >
                Mark read
              </button>
            )}
          </article>
        ))}
      </div>
      {!items.length && <p>No notifications yet.</p>}
    </section>
  );
}
