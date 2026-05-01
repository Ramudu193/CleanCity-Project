import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import CreateComplaint from "./pages/CreateComplaint";
import SubmitProof from "./pages/SubmitProof";
import AdminProofs from "./pages/AdminProofs";
import Leaderboard from "./pages/Leaderboard";
import AdminDashboard from "./pages/AdminDashboard";
import Notifications from "./pages/Notifications";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/create" element={<CreateComplaint />} />
        <Route path="/submit-proof" element={<SubmitProof />} />
        <Route path="/admin-proofs" element={<AdminProofs />} />
        <Route path="/leaderboard" element={<Leaderboard />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />
        <Route path="/notifications" element={<Notifications />} />
        <Route path="/leaderboard" element={<Leaderboard />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;