import React from "react";
import { useState } from "react";
import { api } from "../services/api.js";

export default function SupportPage({ user, flash, setPage }) {
  const [form, setForm] = useState({ subject: "", message: "" });
  const [error, setError] = useState("");

  const submit = async (event) => {
    event.preventDefault();
    if (!user) {
      setPage("login");
      return;
    }
    setError("");
    try {
      await api.createSupport({
        userId: user.userId,
        subject: form.subject,
        message: form.message,
      });
      setForm({ subject: "", message: "" });
      flash("Support request sent.");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <section className="narrow">
      <h1>Support Customer</h1>
      <form className="panel form-stack" onSubmit={submit}>
        {error && <p className="error">{error}</p>}
        <label>
          Subject
          <input
            required
            value={form.subject}
            onChange={(e) => setForm({ ...form, subject: e.target.value })}
          />
        </label>
        <label>
          Message
          <textarea
            required
            rows="5"
            value={form.message}
            onChange={(e) => setForm({ ...form, message: e.target.value })}
          />
        </label>
        <button>Send</button>
      </form>
    </section>
  );
}
