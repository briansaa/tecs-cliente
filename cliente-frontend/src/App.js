import React from 'react';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Home from './pages/home';
import Login from './pages/login';
import ReservaPolideportivos from './pages/polideportivos';
import ReservaLaboratorios from "./pages/laboratorios";

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/home" element={<Home />} />
          <Route path="/" element={<Login />} />
          <Route path="/reservas/polideportivos" element={<ReservaPolideportivos />} />
          <Route path="/reservas/laboratorios" element={<ReservaLaboratorios/>} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
