import { useEffect, useState } from "react";
import API from "../services/API";
import Navbar from "../components/Navbar";
import "../styles/leaderboard.css";

function Leaderboard() {
  const [volunteers, setVolunteers] = useState([]);

  const loadLeaderboard = async () => {
    try {
      const res = await API.get("/volunteers/leaderboard");
      setVolunteers(res.data);
    } catch (err) {
      console.log(err.response?.data || err.message);
      alert("Failed to load leaderboard");
    }
  };

  useEffect(() => {
    loadLeaderboard();
  }, []);

  return (
    <>
      <Navbar />

      <div className="leaderboard-page">
        <h1>Volunteer Leaderboard</h1>
        <p className="leaderboard-subtitle">
          Top contributors helping build a cleaner city.
        </p>

        <div className="leaderboard-list">
          {volunteers.map((v, index) => (
            <div className="leaderboard-card" key={v.id}>
              <div className="rank">#{index + 1}</div>

              <div className="volunteer-info">
                <h3>{v.name}</h3>
                <p>{v.email}</p>
              </div>

              <div className="progress-box">
                <p>{v.completedCount} / 50 cleanups</p>

                <div className="progress-bar">
                  <div
                    className="progress-fill"
                    style={{
                      width: `${Math.min((v.completedCount / 50) * 100, 100)}%`,
                    }}
                  ></div>
                </div>

                {v.rewardEligible && (
                  <span className="reward-badge">Reward Eligible 🎁</span>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

export default Leaderboard;