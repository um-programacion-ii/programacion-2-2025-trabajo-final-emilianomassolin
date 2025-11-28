import dayjs from 'dayjs/esm';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 18174,
  eventId: 17725,
  titulo: 'apud',
  fecha: dayjs('2025-11-20T12:06'),
  filaAsientos: 12521,
  columnaAsientos: 3467,
  precioEntrada: 24625.32,
  tipoNombre: 'behind geez aside',
  tipoDescripcion: 'not best',
};

export const sampleWithPartialData: IEvent = {
  id: 31982,
  eventId: 11721,
  titulo: 'oh',
  fecha: dayjs('2025-11-19T14:45'),
  descripcion: 'optimistically gracefully',
  filaAsientos: 3572,
  columnaAsientos: 2215,
  precioEntrada: 5874.04,
  tipoNombre: 'mysteriously ouch',
  tipoDescripcion: 'gadzooks than',
  integrantes: 'squid packaging',
};

export const sampleWithFullData: IEvent = {
  id: 14744,
  eventId: 17234,
  titulo: 'daintily',
  resumen: 'beside of',
  fecha: dayjs('2025-11-19T15:44'),
  descripcion: 'absent psst',
  filaAsientos: 4253,
  columnaAsientos: 13717,
  tipoEvento: 'uneven uncork lazily',
  direccion: 'astride sharply',
  imagen: 'before weakly',
  precioEntrada: 16999.23,
  tipoNombre: 'filthy nervously',
  tipoDescripcion: 'casket hm',
  integrantes: 'spirit really',
};

export const sampleWithNewData: NewEvent = {
  eventId: 30120,
  titulo: 'between',
  fecha: dayjs('2025-11-20T06:47'),
  filaAsientos: 19701,
  columnaAsientos: 5530,
  precioEntrada: 12289.72,
  tipoNombre: 'and whoever',
  tipoDescripcion: 'giggle ideal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
