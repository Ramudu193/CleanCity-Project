import { useEffect, useState } from "react";
import API from "../services/API";
import Navbar from "../components/Navbar";
import ProofCard from "../components/ProofCard";
import "../styles/proof.css";

function AdminProofs() {
  const [proofs, setProofs] = useState([]);

  const loadProofs = async () => {
    try {
      const res = await API.get("/proofs");
      setProofs(res.data);
    } catch (err) {
      console.log(err.response?.data || err.message);
      alert("Failed to load proofs");
    }
  };

  const approveProof = async (id) => {
    const remark = prompt(
      "Enter approval remark:",
      "Great work! Your cleanup proof has been approved. Keep solving more complaints."
    );

    if (!remark) return;

    try {
      await API.put(`/proofs/${id}/approve`, { remark });
      alert("Proof approved and email sent");
      loadProofs();
    } catch (err) {
      console.log(err.response?.data || err.message);
      alert("Failed to approve proof");
    }
  };

  const rejectProof = async (id) => {
    const reason = prompt(
      "Enter rejection reason:",
      "Video proof is not clear. Please upload a clearer cleanup video."
    );

    if (!reason) return;

    try {
      await API.put(`/proofs/${id}/reject`, { reason });
      alert("Proof rejected and email sent");
      loadProofs();
    } catch (err) {
      console.log(err.response?.data || err.message);
      alert("Failed to reject proof");
    }
  };

  const deleteProof = async (id) => {
    if (!window.confirm("Are you sure you want to delete this proof video?")) {
      return;
    }

    try {
      await API.delete(`/proofs/${id}`);
      alert("Proof deleted successfully");
      loadProofs();
    } catch (err) {
      console.log(err.response?.data || err.message);
      alert("Failed to delete proof");
    }
  };

  useEffect(() => {
    loadProofs();
  }, []);

  return (
    <>
      <Navbar />

      <div className="proof-list-page">
        <h1>Cleanup Proof Reviews</h1>

        <div className="proof-grid">
          {proofs.map((proof) => (
            <ProofCard
              key={proof.id}
              proof={proof}
              onApprove={approveProof}
              onReject={rejectProof}
              onDelete={deleteProof}
            />
          ))}
        </div>
      </div>
    </>
  );
}

export default AdminProofs;