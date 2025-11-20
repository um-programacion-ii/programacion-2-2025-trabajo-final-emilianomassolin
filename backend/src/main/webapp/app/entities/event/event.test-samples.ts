import dayjs from 'dayjs/esm';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 18174,
  eventId: 17725,
  title: 'apud',
  fecha: dayjs('2025-11-20T12:06'),
  filas: 12521,
  columnas: 3467,
};

export const sampleWithPartialData: IEvent = {
  id: 32196,
  eventId: 23203,
  title: 'ugh',
  fecha: dayjs('2025-11-19T15:09'),
  descripcion: 'past oh',
  filas: 6330,
  columnas: 12788,
};

export const sampleWithFullData: IEvent = {
  id: 14744,
  eventId: 17234,
  title: 'daintily',
  subtitle: 'beside of',
  fecha: dayjs('2025-11-19T15:44'),
  descripcion: 'absent psst',
  filas: 4253,
  columnas: 13717,
  tipoEvento: 'uneven uncork lazily',
};

export const sampleWithNewData: NewEvent = {
  eventId: 30120,
  title: 'between',
  fecha: dayjs('2025-11-20T06:47'),
  filas: 19701,
  columnas: 5530,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
