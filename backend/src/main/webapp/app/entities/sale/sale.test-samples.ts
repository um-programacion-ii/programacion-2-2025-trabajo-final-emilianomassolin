import dayjs from 'dayjs/esm';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 2433,
  ventaId: 13918,
  eventId: 3265,
  fechaVenta: dayjs('2025-11-19T22:57'),
  asientos: 'gah rationale bah',
  nombres: 'tepid monumental intermarry',
  precioVenta: 19309.8,
  estado: 'inside',
  resultado: true,
  cantidadAsientos: 20977,
};

export const sampleWithPartialData: ISale = {
  id: 27171,
  ventaId: 14069,
  eventId: 10443,
  fechaVenta: dayjs('2025-11-20T06:28'),
  asientos: 'diversity properly apropos',
  nombres: 'if triumphantly',
  precioVenta: 20271.16,
  estado: 'sport subdued',
  resultado: false,
  cantidadAsientos: 30845,
};

export const sampleWithFullData: ISale = {
  id: 29002,
  ventaId: 1332,
  eventId: 21063,
  fechaVenta: dayjs('2025-11-19T16:21'),
  asientos: 'vice alert',
  nombres: 'honestly',
  precioVenta: 8063.84,
  estado: 'horse yuck',
  resultado: true,
  descripcion: 'slimy',
  cantidadAsientos: 10156,
};

export const sampleWithNewData: NewSale = {
  ventaId: 10215,
  eventId: 11647,
  fechaVenta: dayjs('2025-11-20T12:35'),
  asientos: 'downchange phooey gruesome',
  nombres: 'far jaggedly',
  precioVenta: 6978.25,
  estado: 'till next regarding',
  resultado: true,
  cantidadAsientos: 27929,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
