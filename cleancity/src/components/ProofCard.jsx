import { getVideoUrl } from "../utils/videoUrl";
import "../styles/proof.css";

function ProofCard({ proof, onApprove, onReject, onDelete }) {
  const videoSrc = getVideoUrl(proof.videoUrl);

  return (
    <div className="proof-review-card">
      {videoSrc ? (
        <video controls preload="metadata">
          <source src={videoSrc} type="video/mp4" />
          Your browser does not support the video tag.
        </video>
      ) : (
        <div className="no-video">No video uploaded</div>
      )}

      <div className="proof-info">
        <span className={`proof-status ${proof.status?.toLowerCase()}`}>
          {proof.status}
        </span>

        <h3>{proof.volunteerName}</h3>
        <p>{proof.volunteerEmail}</p>
        <p>{proof.message}</p>

        <p>
          <b>Complaint ID:</b> {proof.complaint?.id}
        </p>

        <p>
          <b>Complaint:</b> {proof.complaint?.description}
        </p>

        <p>
          <b>Location:</b> {proof.complaint?.location}
        </p>

        <div className="proof-actions">
          {proof.status === "PENDING" && (
            <>
              <button onClick={() => onApprove(proof.id)}>Approve</button>

              <button
                className="reject-btn"
                onClick={() => onReject(proof.id)}
              >
                Reject
              </button>
            </>
          )}

          <button className="delete-btn" onClick={() => onDelete(proof.id)}>
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}

export default ProofCard;