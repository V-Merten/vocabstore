import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/loginAndReg.css";

const API_BASE = import.meta.env.VITE_API_BASE ?? "http://localhost:8080";

export default function RegisterPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await fetch(`${API_BASE}/auth/register`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, email, password }),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Login failed");
      }

      // успешная регистрация -> на страницу логина
      navigate("/");
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
        <div className="login-right">
          <div className="login-form-wrapper">
            <h2 className="login-form-title">CREATE A ACCOUNT</h2>

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

              {error && <div className="login-error">{error}</div>}

              <button
                type="submit"
                className="login-button"
                disabled={loading}
              >
                {loading ? "..." : "REGISTER"}
              </button>
            </form>
          </div>
        </div>

        {/* right part */}
        <div className="login-left">
          <div className="login-left-content">
            <h1 className="login-title"> Text </h1>
            <p className="login-text">
             text
            </p>
          </div>

        </div>
      </div>
    </div>
  );
}
