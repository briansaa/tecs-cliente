import ReservaEspacio from '../components/ReservaEspacio';

export default function ReservaLaboratorios() {
  return <ReservaEspacio titulo="Laboratorios" singular="laboratorio" recurso="laboratorios"
    endpoint="reservalaboratorio" campoId="laboratorio_id" />;
}
