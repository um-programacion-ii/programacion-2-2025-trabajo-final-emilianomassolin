import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvent, NewEvent } from '../event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvent for edit and NewEventFormGroupInput for create.
 */
type EventFormGroupInput = IEvent | PartialWithRequiredKeyOf<NewEvent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEvent | NewEvent> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

type EventFormRawValue = FormValueOf<IEvent>;

type NewEventFormRawValue = FormValueOf<NewEvent>;

type EventFormDefaults = Pick<NewEvent, 'id' | 'fecha'>;

type EventFormGroupContent = {
  id: FormControl<EventFormRawValue['id'] | NewEvent['id']>;
  eventId: FormControl<EventFormRawValue['eventId']>;
  title: FormControl<EventFormRawValue['title']>;
  subtitle: FormControl<EventFormRawValue['subtitle']>;
  fecha: FormControl<EventFormRawValue['fecha']>;
  descripcion: FormControl<EventFormRawValue['descripcion']>;
  filas: FormControl<EventFormRawValue['filas']>;
  columnas: FormControl<EventFormRawValue['columnas']>;
  tipoEvento: FormControl<EventFormRawValue['tipoEvento']>;
};

export type EventFormGroup = FormGroup<EventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventFormService {
  createEventFormGroup(event: EventFormGroupInput = { id: null }): EventFormGroup {
    const eventRawValue = this.convertEventToEventRawValue({
      ...this.getFormDefaults(),
      ...event,
    });
    return new FormGroup<EventFormGroupContent>({
      id: new FormControl(
        { value: eventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventId: new FormControl(eventRawValue.eventId, {
        validators: [Validators.required],
      }),
      title: new FormControl(eventRawValue.title, {
        validators: [Validators.required],
      }),
      subtitle: new FormControl(eventRawValue.subtitle),
      fecha: new FormControl(eventRawValue.fecha, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(eventRawValue.descripcion),
      filas: new FormControl(eventRawValue.filas, {
        validators: [Validators.required],
      }),
      columnas: new FormControl(eventRawValue.columnas, {
        validators: [Validators.required],
      }),
      tipoEvento: new FormControl(eventRawValue.tipoEvento),
    });
  }

  getEvent(form: EventFormGroup): IEvent | NewEvent {
    return this.convertEventRawValueToEvent(form.getRawValue() as EventFormRawValue | NewEventFormRawValue);
  }

  resetForm(form: EventFormGroup, event: EventFormGroupInput): void {
    const eventRawValue = this.convertEventToEventRawValue({ ...this.getFormDefaults(), ...event });
    form.reset(
      {
        ...eventRawValue,
        id: { value: eventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EventFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fecha: currentTime,
    };
  }

  private convertEventRawValueToEvent(rawEvent: EventFormRawValue | NewEventFormRawValue): IEvent | NewEvent {
    return {
      ...rawEvent,
      fecha: dayjs(rawEvent.fecha, DATE_TIME_FORMAT),
    };
  }

  private convertEventToEventRawValue(
    event: IEvent | (Partial<NewEvent> & EventFormDefaults),
  ): EventFormRawValue | PartialWithRequiredKeyOf<NewEventFormRawValue> {
    return {
      ...event,
      fecha: event.fecha ? event.fecha.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
