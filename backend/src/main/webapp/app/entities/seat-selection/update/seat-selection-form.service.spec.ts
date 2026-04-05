import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../seat-selection.test-samples';

import { SeatSelectionFormService } from './seat-selection-form.service';

describe('SeatSelection Form Service', () => {
  let service: SeatSelectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SeatSelectionFormService);
  });

  describe('Service methods', () => {
    describe('createSeatSelectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSeatSelectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventId: expect.any(Object),
            userLogin: expect.any(Object),
            asientos: expect.any(Object),
            fechaSeleccion: expect.any(Object),
            expiracion: expect.any(Object),
          }),
        );
      });

      it('passing ISeatSelection should create a new form with FormGroup', () => {
        const formGroup = service.createSeatSelectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventId: expect.any(Object),
            userLogin: expect.any(Object),
            asientos: expect.any(Object),
            fechaSeleccion: expect.any(Object),
            expiracion: expect.any(Object),
          }),
        );
      });
    });

    describe('getSeatSelection', () => {
      it('should return NewSeatSelection for default SeatSelection initial value', () => {
        const formGroup = service.createSeatSelectionFormGroup(sampleWithNewData);

        const seatSelection = service.getSeatSelection(formGroup) as any;

        expect(seatSelection).toMatchObject(sampleWithNewData);
      });

      it('should return NewSeatSelection for empty SeatSelection initial value', () => {
        const formGroup = service.createSeatSelectionFormGroup();

        const seatSelection = service.getSeatSelection(formGroup) as any;

        expect(seatSelection).toMatchObject({});
      });

      it('should return ISeatSelection', () => {
        const formGroup = service.createSeatSelectionFormGroup(sampleWithRequiredData);

        const seatSelection = service.getSeatSelection(formGroup) as any;

        expect(seatSelection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISeatSelection should not enable id FormControl', () => {
        const formGroup = service.createSeatSelectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSeatSelection should disable id FormControl', () => {
        const formGroup = service.createSeatSelectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
