import React from 'react';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Home from './pages/home';
import Login from './pages/login';
import ReservaPolideportivos from './pages/polideportivos';
import ReservaLaboratorios from "./pages/laboratorios";
import RequireAuth from "./components/RequireAuth";

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/home" element={<RequireAuth><Home /></RequireAuth>} />
          <Route path="/" element={<Login />} />
          <Route path="/reservas/polideportivos" element={<RequireAuth><ReservaPolideportivos /></RequireAuth>} />
          <Route path="/reservas/laboratorios" element={<RequireAuth><ReservaLaboratorios /></RequireAuth>} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
