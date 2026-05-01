import { Link, useNavigate } from "react-router-dom";
import "../styles/dashboard.css";

function Navbar() {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("email");
    navigate("/");
  };

  return (
    <nav className="navbar">
      <div className="brand">
        <img src="/images/logo.png" alt="CleanCity Logo" />
        <h2>CleanCity</h2>
      </div>

      <div className="nav-links">
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/create">Create Complaint</Link>
        <Link to="/submit-proof">Submit Proof</Link>
        <Link to="/leaderboard">Leaderboard</Link>
        <Link to="/notifications">Notifications</Link>

        {role === "ADMIN" && <Link to="/admin-dashboard">Admin Stats</Link>}
        {role === "ADMIN" && <Link to="/admin-proofs">Proof Reviews</Link>}

        <button onClick={logout}>Logout</button>
      </div>
    </nav>
  );
}

export default Navbar;