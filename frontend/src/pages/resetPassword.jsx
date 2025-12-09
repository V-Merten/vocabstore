import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import "../styles/loginAndReg.css";
import "../styles/resetPage.css";
import { resetPassword, validateResetToken } from "./api";

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const [token, setToken] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState(null);
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(false);
  const [tokenValid, setTokenValid] = useState(false);

  useEffect(() => {
    const urlToken = searchParams.get("token");
    if (!urlToken) {
      setError("Token is missing");
      setTokenValid(false);
      return;
    }

    setToken(urlToken);
    setError(null);
    setStatus(null);

    const checkToken = async () => {
      try {
        await validateResetToken(urlToken);
        setTokenValid(true);
      } catch (err) {
        setTokenValid(false);
        setError(err.message || "Invalid token");
      }
    };

    checkToken();
  }, [searchParams]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setStatus(null);

    if (!tokenValid) {
      setError("Invalid token");
      return;
    }

    if (newPassword !== confirmPassword) {
      setError("The passwords don't match");
      return;
    }

    setLoading(true);
    try {
      const message = await resetPassword({ token, newPassword });
      setStatus(message || "Password has been reset successfully");
      setNewPassword("");
      setConfirmPassword("");
    } catch (err) {
      setError(err.message || "Failed to reset password");
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
            <span className="login-input-icon">🔒</span>
            <input
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="new password"
              className="login-input"
              required
            />
          </div>

          <div className="login-input-group">
            <span className="login-input-icon">🔒</span>
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              placeholder="confirm password"
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
            disabled={loading || !tokenValid}
          >
            {loading ? "..." : "RESET PASSWORD"}
          </button>
        </form>
      </div>
    </div>
  );
}
