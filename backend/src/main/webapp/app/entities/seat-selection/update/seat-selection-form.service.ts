import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISeatSelection, NewSeatSelection } from '../seat-selection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISeatSelection for edit and NewSeatSelectionFormGroupInput for create.
 */
type SeatSelectionFormGroupInput = ISeatSelection | PartialWithRequiredKeyOf<NewSeatSelection>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISeatSelection | NewSeatSelection> = Omit<T, 'fechaSeleccion' | 'expiracion'> & {
  fechaSeleccion?: string | null;
  expiracion?: string | null;
};

type SeatSelectionFormRawValue = FormValueOf<ISeatSelection>;

type NewSeatSelectionFormRawValue = FormValueOf<NewSeatSelection>;

type SeatSelectionFormDefaults = Pick<NewSeatSelection, 'id' | 'fechaSeleccion' | 'expiracion'>;

type SeatSelectionFormGroupContent = {
  id: FormControl<SeatSelectionFormRawValue['id'] | NewSeatSelection['id']>;
  eventId: FormControl<SeatSelectionFormRawValue['eventId']>;
  userLogin: FormControl<SeatSelectionFormRawValue['userLogin']>;
  asientos: FormControl<SeatSelectionFormRawValue['asientos']>;
  fechaSeleccion: FormControl<SeatSelectionFormRawValue['fechaSeleccion']>;
  expiracion: FormControl<SeatSelectionFormRawValue['expiracion']>;
};

export type SeatSelectionFormGroup = FormGroup<SeatSelectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SeatSelectionFormService {
  createSeatSelectionFormGroup(seatSelection: SeatSelectionFormGroupInput = { id: null }): SeatSelectionFormGroup {
    const seatSelectionRawValue = this.convertSeatSelectionToSeatSelectionRawValue({
      ...this.getFormDefaults(),
      ...seatSelection,
    });
    return new FormGroup<SeatSelectionFormGroupContent>({
      id: new FormControl(
        { value: seatSelectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventId: new FormControl(seatSelectionRawValue.eventId, {
        validators: [Validators.required],
      }),
      userLogin: new FormControl(seatSelectionRawValue.userLogin, {
        validators: [Validators.required],
      }),
      asientos: new FormControl(seatSelectionRawValue.asientos, {
        validators: [Validators.required],
      }),
      fechaSeleccion: new FormControl(seatSelectionRawValue.fechaSeleccion, {
        validators: [Validators.required],
      }),
      expiracion: new FormControl(seatSelectionRawValue.expiracion, {
        validators: [Validators.required],
      }),
    });
  }

  getSeatSelection(form: SeatSelectionFormGroup): ISeatSelection | NewSeatSelection {
    return this.convertSeatSelectionRawValueToSeatSelection(form.getRawValue() as SeatSelectionFormRawValue | NewSeatSelectionFormRawValue);
  }

  resetForm(form: SeatSelectionFormGroup, seatSelection: SeatSelectionFormGroupInput): void {
    const seatSelectionRawValue = this.convertSeatSelectionToSeatSelectionRawValue({ ...this.getFormDefaults(), ...seatSelection });
    form.reset(
      {
        ...seatSelectionRawValue,
        id: { value: seatSelectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SeatSelectionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaSeleccion: currentTime,
      expiracion: currentTime,
    };
  }

  private convertSeatSelectionRawValueToSeatSelection(
    rawSeatSelection: SeatSelectionFormRawValue | NewSeatSelectionFormRawValue,
  ): ISeatSelection | NewSeatSelection {
    return {
      ...rawSeatSelection,
      fechaSeleccion: dayjs(rawSeatSelection.fechaSeleccion, DATE_TIME_FORMAT),
      expiracion: dayjs(rawSeatSelection.expiracion, DATE_TIME_FORMAT),
    };
  }

  private convertSeatSelectionToSeatSelectionRawValue(
    seatSelection: ISeatSelection | (Partial<NewSeatSelection> & SeatSelectionFormDefaults),
  ): SeatSelectionFormRawValue | PartialWithRequiredKeyOf<NewSeatSelectionFormRawValue> {
    return {
      ...seatSelection,
      fechaSeleccion: seatSelection.fechaSeleccion ? seatSelection.fechaSeleccion.format(DATE_TIME_FORMAT) : undefined,
      expiracion: seatSelection.expiracion ? seatSelection.expiracion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
