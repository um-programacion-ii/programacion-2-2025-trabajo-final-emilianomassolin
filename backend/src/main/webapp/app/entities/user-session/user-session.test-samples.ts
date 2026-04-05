import dayjs from 'dayjs/esm';

import { IUserSession, NewUserSession } from './user-session.model';

export const sampleWithRequiredData: IUserSession = {
  id: 14860,
  userLogin: 'vestment',
  step: 13408,
  updatedAt: dayjs('2025-11-20T11:21'),
};

export const sampleWithPartialData: IUserSession = {
  id: 42,
  userLogin: 'basic jazz hmph',
  step: 17068,
  nombres: 'precious',
  updatedAt: dayjs('2025-11-20T10:07'),
};

export const sampleWithFullData: IUserSession = {
  id: 25164,
  userLogin: 'enraged',
  step: 25495,
  eventId: 26071,
  asientos: 'case',
  nombres: 'who rigidly cappelletti',
  updatedAt: dayjs('2025-11-20T05:07'),
};

export const sampleWithNewData: NewUserSession = {
  userLogin: 'modulo',
  step: 15653,
  updatedAt: dayjs('2025-11-20T12:06'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
