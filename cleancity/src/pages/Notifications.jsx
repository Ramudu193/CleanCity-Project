import { useEffect, useState } from "react";
import API from "../services/API";
import Navbar from "../components/Navbar";
import "../styles/notification.css";

function Notifications() {
  const [notifications, setNotifications] = useState([]);

  const loadNotifications = async () => {
    try {
      const email = localStorage.getItem("email");
      const res = await API.get(`/notifications/${email}`);
      setNotifications(res.data);
    } catch (err) {
      console.log(err.response?.data || err.message);
      alert("Failed to load notifications");
    }
  };

  useEffect(() => {
    loadNotifications();
  }, []);

  return (
    <>
      <Navbar />

      <div className="notification-page">
        <h1>Notifications</h1>

        {notifications.length === 0 ? (
          <div className="empty-notification">
            No notifications yet.
          </div>
        ) : (
          <div className="notification-list">
            {notifications.map((n) => (
              <div className="notification-card" key={n.id}>
                <h3>{n.title}</h3>
                <p>{n.message}</p>
                <span>{n.createdAt}</span>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  );
}

export default Notifications;