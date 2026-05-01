import { useState } from "react";
import API from "../services/API";
import { Link, useNavigate } from "react-router-dom";
import "../styles/auth.css";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const res = await API.post("/users/login", {
        email,
        password,
      });

      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", res.data.role);
      localStorage.setItem("email", email);

      alert("Login Successful");
      navigate("/dashboard");
    } catch (err) {
      console.log(err.response?.data || err.message);
      alert("Login Failed");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-left">
        <h1>CleanCity</h1>
        <p>Report city issues. Track complaints. Build a cleaner community.</p>

        <div className="offer-image-box">
          <img src="/images/webimage.jpg" alt="Volunteer Reward Offer" />
        </div>
      </div>

      <div className="auth-card">
        <h2>Welcome Back</h2>

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button onClick={handleLogin}>Login</button>

        <p>
          New user? <Link to="/register">Register</Link>
        </p>
      </div>
    </div>
  );
}

export default Login;