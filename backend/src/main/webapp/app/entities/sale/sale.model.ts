import dayjs from 'dayjs/esm';

export interface ISale {
  id: number;
  ventaId?: number | null;
  eventId?: number | null;
  fechaVenta?: dayjs.Dayjs | null;
  asientos?: string | null;
  nombres?: string | null;
  total?: number | null;
  estado?: string | null;
}

export type NewSale = Omit<ISale, 'id'> & { id: null };
