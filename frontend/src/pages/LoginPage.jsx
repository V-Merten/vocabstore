import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import "../styles/loginAndReg.css";

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await fetch(`/auth/login`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Login failed");
      }

      sessionStorage.setItem("authenticated", "true");

      navigate("/home");
    } catch (err) {
      setError(err.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-root">
      <div className="login-card">
        {/* left part */}
        <div className="login-left">
          <div className="login-left-content">
            <h1 className="login-title"> Text </h1>
            <p className="login-text">
             text
            </p>
          </div>
        </div>

        {/* right part */}
        <div className="login-right">
          <div className="login-form-wrapper">
            <h2 className="login-form-title">USER LOGIN</h2>

            <form onSubmit={handleSubmit} className="login-form">
              <div className="login-input-group">
                <span className="login-input-icon">👤</span>
                <input
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="username"
                  className="login-input"
                  required
                />
              </div>

              <div className="login-input-group">
                <span className="login-input-icon">🔒</span>
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="password"
                  className="login-input"
                  required
                />
              </div>

              <div className="login-options">

                <div className="login-links">
                  <Link to="/forgot-password" className="login-link">
                    Forgot password?
                  </Link>

                  <Link to="/register" className="login-link">
                    Register
                  </Link>
                </div>
              </div>

              {error && <div className="login-error">{error}</div>}

              <button
                type="submit"
                className="login-button"
                disabled={loading}
              >
                {loading ? "..." : "LOGIN"}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
