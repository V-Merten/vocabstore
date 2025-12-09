import { useState } from "react";
import "../styles/loginAndReg.css";
import "../styles/resetPage.css";
import { requestPasswordReset } from "./api";

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState("");
  const [error, setError] = useState(null);
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setStatus(null);
    setLoading(true);

    try {
      const message = await requestPasswordReset(email);
      setStatus(message || "Password reset email sent");
    } catch (err) {
      setError(err.message || "Failed to send reset email");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="reset-root">
      <div className="reset-card">
        <h2 className="login-form-title">RESET PASSWORD</h2>

        <form onSubmit={handleSubmit} className="reset-form">
          <div className="login-input-group">
            <span className="login-input-icon">📧</span>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="email"
              className="login-input"
              required
            />
          </div>

          {(error || status) && (
            <div className={`reset-message ${error ? "error" : "success"}`}>
              {error || status}
            </div>
          )}

          <button
            type="submit"
            className="login-button reset-button"
            disabled={loading}
          >
            {loading ? "..." : "SEND EMAIL"}
          </button>
        </form>
      </div>
    </div>
  );
}
