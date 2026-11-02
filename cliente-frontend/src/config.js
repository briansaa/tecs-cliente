// URL de la API (Spring Boot). Se puede cambiar con REACT_APP_API_URL en .env
export const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8090';

const SESSION_KEY = 'tecsite.usuario';

export const guardarSesion = (usuario) => sessionStorage.setItem(SESSION_KEY, JSON.stringify(usuario));
export const cerrarSesion = () => sessionStorage.removeItem(SESSION_KEY);
export const usuarioActual = () => {
  try {
    return JSON.parse(sessionStorage.getItem(SESSION_KEY));
  } catch {
    return null;
  }
};

// Fecha local YYYY-MM-DD (toISOString usa UTC y en Lima cambia de día a las 7 p. m.)
export const fechaLocal = (date) => {
  const pad = (n) => String(n).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
};
