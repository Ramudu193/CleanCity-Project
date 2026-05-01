function ComplaintCard({ complaint, onUpdate, onDelete }) {
  const getImageUrl = (imageUrl) => {
    if (!imageUrl) return "/images/city1.jpg";

    if (imageUrl.startsWith("http")) {
      return imageUrl;
    }

    if (imageUrl.startsWith("uploads/")) {
      return `http://localhost:8080/${imageUrl}`;
    }

    return `http://localhost:8080/uploads/${imageUrl}`;
  };

  return (
    <div className="complaint-card">
      <img
        src={getImageUrl(complaint.imageUrl)}
        alt="complaint"
        onError={(e) => {
          e.target.src = "/images/city1.jpg";
        }}
      />

      <div className="complaint-content">
        <span className={`badge ${complaint.status?.toLowerCase()}`}>
          {complaint.status}
        </span>

        <h3>{complaint.description}</h3>
        <p>📍 {complaint.location}</p>

        <div className="card-actions">
          <button onClick={() => onUpdate(complaint.id, "IN_PROGRESS")}>
            In Progress
          </button>

          <button onClick={() => onUpdate(complaint.id, "RESOLVED")}>
            Resolve
          </button>

          <button className="delete-btn" onClick={() => onDelete(complaint.id)}>
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}

export default ComplaintCard;