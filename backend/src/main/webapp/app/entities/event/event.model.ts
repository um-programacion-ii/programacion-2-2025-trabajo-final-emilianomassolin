import dayjs from 'dayjs/esm';

export interface IEvent {
  id: number;
  eventId?: number | null;
  titulo?: string | null;
  resumen?: string | null;
  fecha?: dayjs.Dayjs | null;
  descripcion?: string | null;
  filaAsientos?: number | null;
  columnaAsientos?: number | null;
  tipoEvento?: string | null;
  direccion?: string | null;
  imagen?: string | null;
  precioEntrada?: number | null;
  tipoNombre?: string | null;
  tipoDescripcion?: string | null;
  integrantes?: string | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
