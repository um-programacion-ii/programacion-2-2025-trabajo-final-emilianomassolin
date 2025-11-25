import dayjs from 'dayjs/esm';

export interface ISale {
  id: number;
  ventaId?: number | null;
  eventId?: number | null;
  fechaVenta?: dayjs.Dayjs | null;
  asientos?: string | null;
  nombres?: string | null;
  precioVenta?: number | null;
  estado?: string | null;
  resultado?: boolean | null;
  descripcion?: string | null;
  cantidadAsientos?: number | null;
}

export type NewSale = Omit<ISale, 'id'> & { id: null };
