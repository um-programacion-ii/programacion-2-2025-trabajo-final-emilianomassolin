import dayjs from 'dayjs/esm';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 2433,
  ventaId: 13918,
  eventId: 3265,
  fechaVenta: dayjs('2025-11-19T22:57'),
  asientos: 'gah rationale bah',
  nombres: 'tepid monumental intermarry',
  total: 19309.8,
  estado: 'inside',
};

export const sampleWithPartialData: ISale = {
  id: 27333,
  ventaId: 27171,
  eventId: 14069,
  fechaVenta: dayjs('2025-11-19T21:48'),
  asientos: 'badly dazzling ah',
  nombres: 'igloo',
  total: 10275.92,
  estado: 'of leap shocked',
};

export const sampleWithFullData: ISale = {
  id: 29002,
  ventaId: 1332,
  eventId: 21063,
  fechaVenta: dayjs('2025-11-19T16:21'),
  asientos: 'vice alert',
  nombres: 'honestly',
  total: 8063.84,
  estado: 'horse yuck',
};

export const sampleWithNewData: NewSale = {
  ventaId: 10215,
  eventId: 11647,
  fechaVenta: dayjs('2025-11-20T12:35'),
  asientos: 'downchange phooey gruesome',
  nombres: 'far jaggedly',
  total: 6978.25,
  estado: 'till next regarding',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
