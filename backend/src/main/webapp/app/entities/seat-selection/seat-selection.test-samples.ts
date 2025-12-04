import dayjs from 'dayjs/esm';

import { ISeatSelection, NewSeatSelection } from './seat-selection.model';

export const sampleWithRequiredData: ISeatSelection = {
  id: 971,
  eventId: 13828,
  userLogin: 'wildly',
  asientos: 'clamour inscribe truthfully',
  fechaSeleccion: dayjs('2025-11-20T13:28'),
  expiracion: dayjs('2025-11-19T22:20'),
};

export const sampleWithPartialData: ISeatSelection = {
  id: 25852,
  eventId: 28020,
  userLogin: 'nephew',
  asientos: 'cuckoo unless alienated',
  fechaSeleccion: dayjs('2025-11-20T12:33'),
  expiracion: dayjs('2025-11-20T04:24'),
};

export const sampleWithFullData: ISeatSelection = {
  id: 15288,
  eventId: 14840,
  userLogin: 'bootleg',
  asientos: 'skateboard innocent gestate',
  fechaSeleccion: dayjs('2025-11-19T21:05'),
  expiracion: dayjs('2025-11-19T22:28'),
};

export const sampleWithNewData: NewSeatSelection = {
  eventId: 17346,
  userLogin: 'circa over painfully',
  asientos: 'incidentally disloyal including',
  fechaSeleccion: dayjs('2025-11-20T06:12'),
  expiracion: dayjs('2025-11-20T11:34'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
