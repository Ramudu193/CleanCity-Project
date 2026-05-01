import { useEffect, useState } from "react";
import API from "../services/API";
import Navbar from "../components/Navbar";
import ComplaintCard from "../components/ComplaintCard";
import "../styles/dashboard.css";

function Dashboard() {
  const [complaints, setComplaints] = useState([]);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");

  const loadComplaints = async () => {
    try {
      const res = await API.get("/complaints");
      setComplaints(res.data);
    } catch (err) {
      console.log("LOAD ERROR:", err.response?.data || err.message);
      alert("Failed to load complaints");
    }
  };

  const updateComplaint = async (id, status) => {
    try {
      await API.put(`/complaints/${id}`, { status });
      alert("Complaint Updated");
      loadComplaints();
    } catch (err) {
      console.log("UPDATE ERROR:", err.response?.data || err.message);
      alert("Failed to update complaint");
    }
  };

  const deleteComplaint = async (id) => {
    try {
      await API.delete(`/complaints/${id}`);
      alert("Complaint Deleted");
      loadComplaints();
    } catch (err) {
      console.log("DELETE ERROR:", err.response?.data || err.message);
      alert("Failed to delete complaint");
    }
  };

  useEffect(() => {
    loadComplaints();
  }, []);

  const filteredComplaints = complaints.filter((c) => {
    const text = `${c.description} ${c.location}`.toLowerCase();
    const matchesSearch = text.includes(search.toLowerCase());

    const matchesStatus =
      statusFilter === "ALL" || c.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  return (
    <>
      <Navbar />

      <section className="hero">
        <h1>CleanCity Dashboard</h1>
        <p>Monitor, manage and resolve city complaints efficiently.</p>
      </section>

      <div className="stats">
        <div>
          <h2>{complaints.length}</h2>
          <p>Total Complaints</p>
        </div>

        <div>
          <h2>{complaints.filter((c) => c.status === "PENDING").length}</h2>
          <p>Pending</p>
        </div>

        <div>
          <h2>{complaints.filter((c) => c.status === "RESOLVED").length}</h2>
          <p>Resolved</p>
        </div>
      </div>

      <div className="filter-box">
        <input
          type="text"
          placeholder="Search by description or location..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option value="ALL">All Status</option>
          <option value="PENDING">Pending</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="RESOLVED">Resolved</option>
        </select>
      </div>

      <div className="complaint-grid">
        {filteredComplaints.length > 0 ? (
          filteredComplaints.map((complaint) => (
            <ComplaintCard
              key={complaint.id}
              complaint={complaint}
              onUpdate={updateComplaint}
              onDelete={deleteComplaint}
            />
          ))
        ) : (
          <p className="empty-message">No complaints found</p>
        )}
      </div>
    </>
  );
}

export default Dashboard;