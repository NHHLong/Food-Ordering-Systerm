import React from "react";

export default function Notice({ message, type = "success", onClose }) {
  if (!message) return null;
  return (
    <div className={`notice ${type}`}>
      <span>{message}</span>
      <button type="button" onClick={onClose}>
        x
      </button>
    </div>
  );
}
