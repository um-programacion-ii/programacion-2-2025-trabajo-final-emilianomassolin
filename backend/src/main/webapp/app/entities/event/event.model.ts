import dayjs from 'dayjs/esm';

export interface IEvent {
  id: number;
  eventId?: number | null;
  title?: string | null;
  subtitle?: string | null;
  fecha?: dayjs.Dayjs | null;
  descripcion?: string | null;
  filas?: number | null;
  columnas?: number | null;
  tipoEvento?: string | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
