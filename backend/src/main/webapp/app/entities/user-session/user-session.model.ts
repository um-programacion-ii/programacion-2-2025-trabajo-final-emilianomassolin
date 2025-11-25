import dayjs from 'dayjs/esm';

export interface IUserSession {
  id: number;
  userLogin?: string | null;
  step?: number | null;
  eventId?: number | null;
  asientos?: string | null;
  nombres?: string | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewUserSession = Omit<IUserSession, 'id'> & { id: null };
