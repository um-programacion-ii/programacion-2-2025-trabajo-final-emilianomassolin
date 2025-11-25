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
  titulo: FormControl<EventFormRawValue['titulo']>;
  resumen: FormControl<EventFormRawValue['resumen']>;
  fecha: FormControl<EventFormRawValue['fecha']>;
  descripcion: FormControl<EventFormRawValue['descripcion']>;
  filaAsientos: FormControl<EventFormRawValue['filaAsientos']>;
  columnaAsientos: FormControl<EventFormRawValue['columnaAsientos']>;
  tipoEvento: FormControl<EventFormRawValue['tipoEvento']>;
  direccion: FormControl<EventFormRawValue['direccion']>;
  imagen: FormControl<EventFormRawValue['imagen']>;
  precioEntrada: FormControl<EventFormRawValue['precioEntrada']>;
  tipoNombre: FormControl<EventFormRawValue['tipoNombre']>;
  tipoDescripcion: FormControl<EventFormRawValue['tipoDescripcion']>;
  integrantes: FormControl<EventFormRawValue['integrantes']>;
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
      titulo: new FormControl(eventRawValue.titulo, {
        validators: [Validators.required],
      }),
      resumen: new FormControl(eventRawValue.resumen),
      fecha: new FormControl(eventRawValue.fecha, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(eventRawValue.descripcion),
      filaAsientos: new FormControl(eventRawValue.filaAsientos, {
        validators: [Validators.required],
      }),
      columnaAsientos: new FormControl(eventRawValue.columnaAsientos, {
        validators: [Validators.required],
      }),
      tipoEvento: new FormControl(eventRawValue.tipoEvento),
      direccion: new FormControl(eventRawValue.direccion),
      imagen: new FormControl(eventRawValue.imagen),
      precioEntrada: new FormControl(eventRawValue.precioEntrada, {
        validators: [Validators.required],
      }),
      tipoNombre: new FormControl(eventRawValue.tipoNombre, {
        validators: [Validators.required],
      }),
      tipoDescripcion: new FormControl(eventRawValue.tipoDescripcion, {
        validators: [Validators.required],
      }),
      integrantes: new FormControl(eventRawValue.integrantes),
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
