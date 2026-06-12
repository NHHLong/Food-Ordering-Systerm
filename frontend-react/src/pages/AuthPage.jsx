import React from "react";
import { useState } from "react";
import { api } from "../services/api.js";

export default function AuthPage({ mode, setUser, setPage, flash }) {
  const [form, setForm] = useState({
    username: "",
    password: "",
    email: "",
    phone: "",
  });
  const [error, setError] = useState("");
  const isLogin = mode === "login";

  const submit = async (event) => {
    event.preventDefault();
    setError("");
    try {
      if (isLogin) {
        const user = await api.login({
          username: form.username,
          password: form.password,
        });
        setUser(user);
        setPage("menu");
        flash("Login success.");
      } else {
        await api.register(form);
        setPage("login");
        flash("Register success. Please login.");
      }
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <section className="narrow">
      <h1>{isLogin ? "Login" : "Register"}</h1>
      <form className="panel form-stack" onSubmit={submit}>
        {error && <p className="error">{error}</p>}
        <label>
          Username
          <input
            required
            value={form.username}
            onChange={(e) => setForm({ ...form, username: e.target.value })}
          />
        </label>
        <label>
          Password
          <input
            required
            type="password"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
          />
        </label>
        {!isLogin && (
          <>
            <label>
              Email
              <input
                value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })}
              />
            </label>
            <label>
              Phone
              <input
                value={form.phone}
                onChange={(e) => setForm({ ...form, phone: e.target.value })}
              />
            </label>
          </>
        )}
        <button>{isLogin ? "Login" : "Register"}</button>
      </form>
    </section>
  );
}
