import React, { useEffect, useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../assets/logo.png';
import imagenIzquierda from '../assets/layout2.png';
import imagenDerecha from '../assets/layout1.png';
import { API_URL, cerrarSesion, fechaLocal, usuarioActual } from '../config';

const DIAS_ADELANTE = 14;

/**
 * Pantalla de reserva reutilizable para laboratorios y polideportivos.
 * @param titulo     texto visible ("Laboratorios")
 * @param recurso    ruta de la API para listar espacios ("laboratorios")
 * @param endpoint   ruta de la API para reservar ("reservalaboratorio")
 * @param campoId    nombre del campo del espacio en la reserva ("laboratorio_id")
 * @param singular   nombre singular para mensajes ("laboratorio")
 */
export default function ReservaEspacio({ titulo, recurso, endpoint, campoId, singular }) {
  const navigate = useNavigate();
  const usuario = usuarioActual();

  const hoy = new Date();
  const limite = new Date();
  limite.setDate(hoy.getDate() + DIAS_ADELANTE);

  const [fecha, setFecha] = useState(hoy);
  const [espacios, setEspacios] = useState([]);
  const [horarios, setHorarios] = useState([]);
  const [espacioId, setEspacioId] = useState('');
  const [horarioId, setHorarioId] = useState('');
  const [enviando, setEnviando] = useState(false);
  const [mensaje, setMensaje] = useState(null);
  const [resumen, setResumen] = useState(null);

  useEffect(() => {
    Promise.all([axios.get(`${API_URL}/${recurso}/getAll`), axios.get(`${API_URL}/horarios/getAll`)])
      .then(([resEspacios, resHorarios]) => {
        setEspacios(resEspacios.data);
        setHorarios(resHorarios.data);
      })
      .catch(() => setMensaje({ tipo: 'danger', texto: 'No se pudo conectar con el servidor.' }));
  }, [recurso]);

  const reservar = async () => {
    if (!espacioId || !horarioId) {
      setMensaje({ tipo: 'warning', texto: `Elige un ${singular} y un horario.` });
      return;
    }
    setEnviando(true);
    setMensaje(null);
    try {
      await axios.post(`${API_URL}/${endpoint}/store`, {
        fecha: fechaLocal(fecha),
        [campoId]: Number(espacioId),
        horario_id: Number(horarioId),
        usuario_id: usuario.id,
      });
      const espacio = espacios.find((e) => e.id === Number(espacioId));
      const horario = horarios.find((h) => h.id === Number(horarioId));
      setResumen({ fecha: fecha.toLocaleDateString('es-PE'), espacio: espacio?.nombre, horario: `${horario?.hora_inicio} - ${horario?.hora_fin}` });
      setMensaje({ tipo: 'success', texto: 'Reserva realizada con éxito.' });
      setEspacioId('');
      setHorarioId('');
    } catch (error) {
      const texto = error.response?.data?.mensaje || 'No se pudo realizar la reserva. Intenta de nuevo.';
      setMensaje({ tipo: error.response?.status === 409 ? 'warning' : 'danger', texto });
    } finally {
      setEnviando(false);
    }
  };

  const salir = () => {
    cerrarSesion();
    navigate('/');
  };

  return (
    <div className="container mt-4">
      <div className="d-flex align-items-center justify-content-between">
        <Link to="/home"><img src={logo} alt="TECSITE" width="100" height="100" /></Link>
        <div className="text-end">
          <div className="small text-muted">Sesión: <strong>{usuario.codigo}</strong></div>
          <button className="btn btn-link p-0" onClick={salir}>Cerrar sesión</button>
        </div>
      </div>

      <img src={imagenIzquierda} alt="" aria-hidden="true" className="w-15 h-70 position-absolute start-0"
        style={{ objectFit: 'cover', zIndex: '-1', transform: 'scaleX(-1)' }} />
      <img src={imagenDerecha} alt="" aria-hidden="true" className="w-15 h-70 position-absolute end-0"
        style={{ objectFit: 'cover', zIndex: '-1' }} />

      <h2 className="text-center my-3">Reserva de {titulo}</h2>

      <div className="d-flex flex-wrap justify-content-center align-items-start gap-3 mt-4">
        <Calendar onChange={setFecha} value={fecha} minDate={hoy} maxDate={limite} locale="es-PE" />

        <div className="d-flex flex-column gap-3" style={{ minWidth: 260 }}>
          <div>
            <label htmlFor="espacio" className="form-label">{titulo}</label>
            <select id="espacio" className="form-select" value={espacioId} onChange={(e) => setEspacioId(e.target.value)}>
              <option value="">Selecciona un {singular}</option>
              {espacios.map((e) => <option key={e.id} value={e.id}>{e.nombre}</option>)}
            </select>
          </div>
          <div>
            <label htmlFor="horario" className="form-label">Horario</label>
            <select id="horario" className="form-select" value={horarioId} onChange={(e) => setHorarioId(e.target.value)}>
              <option value="">Selecciona un horario</option>
              {horarios.map((h) => <option key={h.id} value={h.id}>{`${h.hora_inicio} - ${h.hora_fin}`}</option>)}
            </select>
          </div>
          <div className="small text-muted">Fecha: {fecha.toLocaleDateString('es-PE', { weekday: 'long', day: 'numeric', month: 'long' })}</div>
          <button className="btn btn-primary" onClick={reservar} disabled={enviando}>
            {enviando ? 'Reservando…' : 'Reservar'}
          </button>
        </div>
      </div>

      {mensaje && <div className={`mt-4 alert alert-${mensaje.tipo}`} role="alert">{mensaje.texto}</div>}

      {resumen && (
        <div className="card mt-3">
          <div className="card-body">
            <h5 className="card-title">Resumen de la reserva</h5>
            <p className="mb-1">Fecha: {resumen.fecha}</p>
            <p className="mb-1">{titulo.slice(0, -1)}: {resumen.espacio}</p>
            <p className="mb-1">Horario: {resumen.horario}</p>
            <p className="mb-0">Alumno: {usuario.codigo}</p>
          </div>
        </div>
      )}

      <Link to="/home" className="btn btn-outline-primary mt-3">Volver a inicio</Link>
    </div>
  );
}
