import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import axios from 'axios';
import { Link } from 'react-router-dom';
import logo from '../assets/logo.png';
import loginImageLeft from '../assets/layout2.png';
import loginImageRight from '../assets/layout1.png';

function ReservaLaboratorios() {
    const [selectedDate, setSelectedDate] = useState(new Date());

    const [horarios, setHorarios] = useState([]);
    const [laboratorios, setLaboratorios] = useState([]);
    const [usuarios, setUsuarios] = useState([]);

    const [selectedLaboratorio, setSelectedLaboratorio] = useState(null);
    const [selectedHorario, setSelectedHorario] = useState(null);
    const [selectedUsuario, setSelectedUsuario] = useState(null);

    const [reservaMessage, setReservaMessage] = useState(null);
    const [resumenReserva, setResumenReserva] = useState(null);

    useEffect(() => {
        const fetchHorarios = async () => {
            try {
                const response = await axios.get('http://localhost:8090/horarios/getAll');
                setHorarios(response.data);
            } catch (error) {
                console.error('Error al obtener los horarios disponibles', error);
            }
        };

        const fetchLaboratorios = async () => {
            try {
                const response = await axios.get('http://localhost:8090/laboratorios/getAll');
                setLaboratorios(response.data);
            } catch (error) {
                console.error('Error al obtener los laboratorios', error);
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
        fetchLaboratorios();
        fetchUsuarios();
    }, [selectedHorario, selectedLaboratorio, selectedUsuario]);

    const handleDateChange = (date) => {
        setSelectedDate(date);
    };

    const handleReserva = async () => {
        try {
            const reservaData = {
                fecha: selectedDate,
                laboratorio_id: selectedLaboratorio,
                horario_id: selectedHorario,
                usuario_id: selectedUsuario,
            };

            const response = await axios.post('http://localhost:8090/reservalaboratorio/store', reservaData);

            if (response.status === 200 || response.status === 201) {
                setReservaMessage('Reserva de laboratorio realizada con éxito.');

                console.log('Laboratorios:', laboratorios);
                console.log('Horarios:', horarios);
                console.log('Usuarios:', usuarios);

                const nombreLaboratorio = laboratorios.find((lab) => lab.id === parseInt(selectedLaboratorio))?.nombre || 'Laboratorio Desconocido';
                const horaInicio = horarios.find((hor) => hor.id === parseInt(selectedHorario))?.hora_inicio || 'Horario Desconocido';
                const codigoUsuario = usuarios.find((usu) => usu.id === parseInt(selectedUsuario))?.codigo || 'Codigo Desconocido';

                setResumenReserva({
                    fecha: selectedDate,
                    laboratorio: nombreLaboratorio,
                    horario: horaInicio,
                    usuario: codigoUsuario,
                });

                console.log('Reserva de laboratorio realizada:', selectedLaboratorio);

                setSelectedDate(new Date());
                setSelectedLaboratorio(null);
                setSelectedHorario(null);
                setSelectedUsuario(null);

                setTimeout(() => {
                    setResumenReserva(null);
                }, 5000);
            } else {
                setReservaMessage(`Error al realizar la reserva de laboratorio. Estado: ${response.status}`);
                console.error('Error al realizar la reserva de laboratorio. Estado:', response.status);
            }
        } catch (error) {
            setReservaMessage('Error al realizar la reserva de laboratorio. Consulta los detalles del error en la consola.');
            console.error('Error al realizar la reserva de laboratorio', error);
            console.error('Detalles completos del error:', error.response);
        }
    };

    return (
        <div className="container mt-4">
            <Link to="/home" className="navbar-brand d-flex align-items-center text-center">
                <img src={logo} alt="TECSITE Logo" width="100" height="100" className="mx-auto"/>
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

            <img src={loginImageLeft} alt="Login" className="w-15 h-70 position-absolute start-0"
                 style={{ objectFit: 'cover', zIndex: '-1', transform: 'scaleX(-1)' }}/>

            <img src={loginImageRight} alt="Login" className="w-15 h-70 position-absolute end-0"
                 style={{ objectFit: 'cover', zIndex: '-1' }}/>

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
                    <select className="form-select" onChange={(e) => setSelectedLaboratorio(e.target.value)}>
                        <option value="">Selecciona un laboratorio</option>
                        {laboratorios.map((laboratorio) => (
                            <option key={laboratorio.id} value={laboratorio.id}>
                                {laboratorio.nombre}
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

            {reservaMessage && (
                <div className={`mt-3 alert ${reservaMessage.includes('éxito') ? 'alert-success' : 'alert-danger'}`}
                     role="alert">
                    {reservaMessage}
                </div>
            )}
            <Link to="/home" className="btn btn-primary mt-3">
                Volver a Inicio
            </Link>
            {resumenReserva && (
                <div className="mt-3">
                    <h5>Resumen de la Reserva:</h5>
                    <p>Fecha: {resumenReserva.fecha.toLocaleDateString()}</p>
                    <p>Laboratorio: {resumenReserva.laboratorio}</p>
                    <p>Horario: {resumenReserva.horario}</p>
                    <p>Usuario: {resumenReserva.usuario}</p>

                </div>
            )}
        </div>
    );
}

export default ReservaLaboratorios;
