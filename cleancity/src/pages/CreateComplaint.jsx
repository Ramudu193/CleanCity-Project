import { useState } from "react";
import API from "../services/API";
import Navbar from "../components/Navbar";
import "../styles/form.css";

function CreateComplaint() {
  const [description, setDescription] = useState("");
  const [location, setLocation] = useState("");
  const [status, setStatus] = useState("PENDING");
  const [image, setImage] = useState(null);
  const [preview, setPreview] = useState("");

  const handleImage = (e) => {
    const selectedImage = e.target.files[0];

    if (selectedImage) {
      setImage(selectedImage);
      setPreview(URL.createObjectURL(selectedImage));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!description.trim()) {
      alert("Please enter complaint description");
      return;
    }

    if (!location.trim()) {
      alert("Please enter location");
      return;
    }

    if (!image) {
      alert("Please select an image");
      return;
    }

    const formData = new FormData();
    formData.append("description", description);
    formData.append("location", location);
    formData.append("status", status);
    formData.append("image", image);

    try {
      const res = await API.post("/complaints", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      console.log("Complaint saved:", res.data);
      alert("Complaint Submitted Successfully");

      setDescription("");
      setLocation("");
      setStatus("PENDING");
      setImage(null);
      setPreview("");

      const imageInput = document.getElementById("imageInput");
      if (imageInput) {
        imageInput.value = "";
      }
    } catch (err) {
      console.log("SUBMIT ERROR STATUS:", err.response?.status);
      console.log("SUBMIT ERROR DATA:", err.response?.data || err.message);
      alert("Failed to submit complaint");
    }
  };

  return (
    <>
      <Navbar />

      <div className="form-page">
        <div className="form-card">
          <h2>Create Complaint</h2>

          <form onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="Complaint description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />

            <input
              type="text"
              placeholder="Location"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
            />

            <select value={status} onChange={(e) => setStatus(e.target.value)}>
              <option value="PENDING">PENDING</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="RESOLVED">RESOLVED</option>
            </select>

            <input
              id="imageInput"
              type="file"
              accept="image/*"
              onChange={handleImage}
            />

            {preview && (
              <img src={preview} alt="preview" className="preview-img" />
            )}

            <button type="submit">Submit Complaint</button>
          </form>
        </div>
      </div>
    </>
  );
}

export default CreateComplaint;