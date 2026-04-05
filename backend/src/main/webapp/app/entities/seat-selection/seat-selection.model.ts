import dayjs from 'dayjs/esm';

export interface ISeatSelection {
  id: number;
  eventId?: number | null;
  userLogin?: string | null;
  asientos?: string | null;
  fechaSeleccion?: dayjs.Dayjs | null;
  expiracion?: dayjs.Dayjs | null;
}

export type NewSeatSelection = Omit<ISeatSelection, 'id'> & { id: null };
