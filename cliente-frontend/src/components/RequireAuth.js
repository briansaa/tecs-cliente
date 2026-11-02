import { Navigate } from 'react-router-dom';
import { usuarioActual } from '../config';

// Si no hay sesión, manda al login.
export default function RequireAuth({ children }) {
  return usuarioActual() ? children : <Navigate to="/" replace />;
}
