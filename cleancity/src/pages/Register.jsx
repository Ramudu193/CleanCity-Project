import { useState } from "react";
import API from "../services/API";
import { Link, useNavigate } from "react-router-dom";
import "../styles/auth.css";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      await API.post("/users/register", {
        name: name,
        email: email,
        password: password,
        role: "USER"
      });

      alert("Registration Successful");
      navigate("/");
    } catch (err) {
      console.log("REGISTER ERROR:", err.response?.data || err.message);
      alert("Registration Failed");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-left">
        <h1>Join CleanCity</h1>
        <p>Create an account and start reporting public issues easily.</p>
      </div>

      <div className="auth-card">
        <h2>Create Account</h2>

        <input placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} />
        <input placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />

        <button onClick={handleRegister}>Register</button>

        <p>
          Already have account? <Link to="/">Login</Link>
        </p>
      </div>
    </div>
  );
}

export default Register;