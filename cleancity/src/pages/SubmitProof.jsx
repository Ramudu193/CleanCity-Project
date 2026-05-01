import { useState } from "react";
import API from "../services/API";
import Navbar from "../components/Navbar";
import "../styles/proof.css";

function SubmitProof() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [complaintId, setComplaintId] = useState("");
  const [message, setMessage] = useState("");
  const [video, setVideo] = useState(null);

  const submitProof = async (e) => {
    e.preventDefault();

    if (!name || !email || !complaintId || !message || !video) {
      alert("Please fill all fields and upload video");
      return;
    }

    const formData = new FormData();
    formData.append("name", name);
    formData.append("email", email);
    formData.append("complaintId", complaintId);
    formData.append("message", message);
    formData.append("video", video);

    try {
      await API.post("/proofs/submit", formData);

      alert("Proof submitted successfully");

      setName("");
      setEmail("");
      setComplaintId("");
      setMessage("");
      setVideo(null);

      document.getElementById("videoInput").value = "";
    } catch (err) {
      console.log("SUBMIT PROOF ERROR:", err.response?.data || err.message);
      alert("Failed to submit proof");
    }
  };

  return (
    <>
      <Navbar />

      <div className="proof-page">
        <div className="proof-card">
          <h2>Submit Cleanup Proof</h2>

          <form onSubmit={submitProof}>
            <input
              placeholder="Volunteer Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <input
              placeholder="Volunteer Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <input
              placeholder="Complaint ID"
              value={complaintId}
              onChange={(e) => setComplaintId(e.target.value)}
            />

            <textarea
              placeholder="Message"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
            />

            <input
              id="videoInput"
              type="file"
              accept="video/mp4"
              onChange={(e) => setVideo(e.target.files[0])}
            />

            <button type="submit">Submit Proof</button>
          </form>
        </div>
      </div>
    </>
  );
}

export default SubmitProof;