import { useEffect, useState } from "react";
import API from "../services/API";
import Navbar from "../components/Navbar";
import {
  Bar,
  Pie
} from "react-chartjs-2";
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  ArcElement,
  Tooltip,
  Legend
} from "chart.js";

import "../styles/admin.css";

ChartJS.register(
  BarElement,
  CategoryScale,
  LinearScale,
  ArcElement,
  Tooltip,
  Legend
);

function AdminDashboard() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const res = await API.get("/admin/stats");
      setStats(res.data);
    } catch (err) {
      console.log(err.response?.data || err.message);
      alert("Failed to load admin stats");
    }
  };

  if (!stats) {
    return (
      <>
        <Navbar />
        <div className="admin-page">
          <h1>Loading...</h1>
        </div>
      </>
    );
  }

  const complaintData = {
    labels: ["Pending", "Resolved"],
    datasets: [
      {
        label: "Complaints",
        data: [stats.pendingComplaints, stats.resolvedComplaints],
      },
    ],
  };

  const proofData = {
    labels: ["Approved", "Rejected", "Pending"],
    datasets: [
      {
        label: "Proofs",
        data: [
          stats.approvedProofs,
          stats.rejectedProofs,
          stats.pendingProofs,
        ],
      },
    ],
  };

  return (
    <>
      <Navbar />

      <div className="admin-page">
        <h1>Admin Dashboard</h1>

        <div className="admin-grid">
          <div className="admin-card">
            <h2>{stats.totalComplaints}</h2>
            <p>Total Complaints</p>
          </div>

          <div className="admin-card pending">
            <h2>{stats.pendingComplaints}</h2>
            <p>Pending Complaints</p>
          </div>

          <div className="admin-card resolved">
            <h2>{stats.resolvedComplaints}</h2>
            <p>Resolved Complaints</p>
          </div>

          <div className="admin-card">
            <h2>{stats.totalVolunteers}</h2>
            <p>Total Volunteers</p>
          </div>
        </div>

        <div className="chart-grid">
          <div className="chart-box">
            <h3>Complaint Status</h3>
            <Bar data={complaintData} />
          </div>

          <div className="chart-box">
            <h3>Proof Status</h3>
            <Pie data={proofData} />
          </div>
        </div>
      </div>
    </>
  );
}

export default AdminDashboard;