import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import axios from 'axios';
import { Link, useLocation } from 'react-router-dom';
import logo from '../assets/logo.png';

function ReservaPolideportivos() {
  const [selectedDate, setSelectedDate] = useState(new Date());

  const [horarios, setHorarios] = useState([]);
  const [polideportivos, setPolideportivos] = useState([]);
  const [usuarios, setUsuarios] = useState([]);

  const [selectedPolideportivo, setSelectedPolideportivo] = useState(null);
  const [selectedUsuario, setSelectedUsuario] = useState(null);
  const [selectedHorario, setSelectedHorario] = useState(null);

  const [reservaMessage, setReservaMessage] = useState(null);
  const [resumenReserva, setResumenReserva] = useState(null);

  const location = useLocation();
  const usuarioId = new URLSearchParams(location.search).get('usuario_id');

  useEffect(() => {
    const fetchHorarios = async () => {
      try {
        const response = await axios.get('http://localhost:8090/horarios/getAll');
        setHorarios(response.data);
      } catch (error) {
        console.error('Error al obtener los horarios disponibles', error);
      }
    };

    const fetchPolideportivos = async () => {
      try {
        const response = await axios.get('http://localhost:8090/polideportivos/getAll');
        setPolideportivos(response.data);
      } catch (error) {
        console.error('Error al obtener los polideportivos', error);
      }
    };

    const fetchUsuarios = async () => {
      try {
        const response = await axios.get('http://localhost:8090/clientes/getAll');
        setUsuarios(response.data);
      } catch (error) {
        console.error('Error al obtener los Usuarios', error);
      }
    };


    fetchHorarios();
    fetchPolideportivos();
    fetchUsuarios();
  }, [selectedHorario, selectedPolideportivo, selectedUsuario]);

  const handleDateChange = (date) => {
    setSelectedDate(date);
  };

  const handleReserva = async () => {
    try {
      const reservaData = {
        fecha: selectedDate,
        polideportivo_id: selectedPolideportivo,
        horario_id: selectedHorario,
        usuario_id: selectedUsuario,
      };

      const response = await axios.post('http://localhost:8090/reservapolideportivo/store', reservaData);

      if (response.status === 200 || response.status === 201) {
        setReservaMessage('Reserva de polideportivo realizada con éxito.');

        const nombrePolideportivo = polideportivos.find((pol) => pol.id === parseInt(selectedPolideportivo))?.nombre || 'Polideportivo Desconocido';
        const horaInicio = horarios.find((hor) => hor.id === parseInt(selectedHorario))?.hora_inicio || 'Horario Desconocido';
        const codigoUsuario = usuarios.find((usu) => usu.id === parseInt(selectedUsuario))?.codigo || 'Codigo Desconocido';

        setResumenReserva({
          fecha: selectedDate,
          polideportivo: nombrePolideportivo,
          horario: horaInicio,
          usuario: codigoUsuario,
        });

        console.log('Reserva de polideportivo realizada:', selectedPolideportivo);

        setSelectedDate(new Date());
        setSelectedPolideportivo(null);
        setSelectedHorario(null);
        setSelectedUsuario(null);


        setTimeout(() => {
          setResumenReserva(null);
        }, 5000);
      } else {
        setReservaMessage(`Error al realizar la reserva de polideportivo. Estado: ${response.status}`);
        console.error('Error al realizar la reserva de polideportivo. Estado:', response.status);
      }
    } catch (error) {
      setReservaMessage('Error al realizar la reserva de polideportivo. Consulta los detalles del error en la consola.');
      console.error('Error al realizar la reserva de polideportivo', error);
      console.error('Detalles completos del error:', error.response);
    }
  };

  return (
      <div className="container mt-4">
        <Link to="/home" className="navbar-brand d-flex align-items-center text-center">
          <img src={logo} alt="TECSITE Logo" width="100" height="100" className="mx-auto" />
        </Link>
        <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="d-flex justify-content-center align-items-center mt-4">
          <div className="me-3">
            <Calendar
                onChange={handleDateChange}
                value={selectedDate}
                minDate={new Date()}
                maxDate={new Date()}
            />
          </div>

          <div className="me-3">
            <select className="form-select" onChange={(e) => setSelectedPolideportivo(e.target.value)}>
              <option value="">Selecciona un Polideportivo</option>
              {polideportivos.map((polideportivo) => (
                  <option key={polideportivo.id} value={polideportivo.id}>
                    {polideportivo.nombre}
                  </option>
              ))}
            </select>
          </div>

          <div className="me-3">
            <select className="form-select" onChange={(e) => setSelectedHorario(e.target.value)}>
              <option value="">Selecciona un horario</option>
              {horarios.map((horario) => (
                  <option key={horario.id} value={horario.id}>
                    {`${horario.hora_inicio} - ${horario.hora_fin}`}
                  </option>
              ))}
            </select>
          </div>

          <div className="me-3">
            <select className="form-select" onChange={(e) => setSelectedUsuario(e.target.value)}>
              <option value="">Código</option>
              {usuarios.map((usuario) => (
                  <option key={usuario.id} value={usuario.id}>
                    {`${usuario.codigo}`}
                  </option>
              ))}
            </select>
          </div>
          <button className="btn btn-primary" onClick={handleReserva}>
            Reservar
          </button>
        </div>
        <Link to="/home" className="btn btn-primary mt-3">
          Volver a Inicio
        </Link>

        {reservaMessage && (
            <div className={`mt-3 alert ${reservaMessage.includes('éxito') ? 'alert-success' : 'alert-danger'}`}
                 role="alert">
              {reservaMessage}
            </div>
        )}

        {resumenReserva && (
            <div className="mt-3">
              <h5>Resumen de la Reserva:</h5>
              <p>Fecha: {resumenReserva.fecha.toLocaleDateString()}</p>
              <p>Polideportivo: {resumenReserva.polideportivo}</p>
              <p>Horario: {resumenReserva.horario}</p>
              <p>Usuario: {resumenReserva.usuario}</p>
            </div>
        )}
      </div>
  );
}

export default ReservaPolideportivos;
