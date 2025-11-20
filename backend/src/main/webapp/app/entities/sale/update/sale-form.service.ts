import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISale, NewSale } from '../sale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISale for edit and NewSaleFormGroupInput for create.
 */
type SaleFormGroupInput = ISale | PartialWithRequiredKeyOf<NewSale>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISale | NewSale> = Omit<T, 'fechaVenta'> & {
  fechaVenta?: string | null;
};

type SaleFormRawValue = FormValueOf<ISale>;

type NewSaleFormRawValue = FormValueOf<NewSale>;

type SaleFormDefaults = Pick<NewSale, 'id' | 'fechaVenta'>;

type SaleFormGroupContent = {
  id: FormControl<SaleFormRawValue['id'] | NewSale['id']>;
  ventaId: FormControl<SaleFormRawValue['ventaId']>;
  eventId: FormControl<SaleFormRawValue['eventId']>;
  fechaVenta: FormControl<SaleFormRawValue['fechaVenta']>;
  asientos: FormControl<SaleFormRawValue['asientos']>;
  nombres: FormControl<SaleFormRawValue['nombres']>;
  total: FormControl<SaleFormRawValue['total']>;
  estado: FormControl<SaleFormRawValue['estado']>;
};

export type SaleFormGroup = FormGroup<SaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SaleFormService {
  createSaleFormGroup(sale: SaleFormGroupInput = { id: null }): SaleFormGroup {
    const saleRawValue = this.convertSaleToSaleRawValue({
      ...this.getFormDefaults(),
      ...sale,
    });
    return new FormGroup<SaleFormGroupContent>({
      id: new FormControl(
        { value: saleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ventaId: new FormControl(saleRawValue.ventaId, {
        validators: [Validators.required],
      }),
      eventId: new FormControl(saleRawValue.eventId, {
        validators: [Validators.required],
      }),
      fechaVenta: new FormControl(saleRawValue.fechaVenta, {
        validators: [Validators.required],
      }),
      asientos: new FormControl(saleRawValue.asientos, {
        validators: [Validators.required],
      }),
      nombres: new FormControl(saleRawValue.nombres, {
        validators: [Validators.required],
      }),
      total: new FormControl(saleRawValue.total, {
        validators: [Validators.required],
      }),
      estado: new FormControl(saleRawValue.estado, {
        validators: [Validators.required],
      }),
    });
  }

  getSale(form: SaleFormGroup): ISale | NewSale {
    return this.convertSaleRawValueToSale(form.getRawValue() as SaleFormRawValue | NewSaleFormRawValue);
  }

  resetForm(form: SaleFormGroup, sale: SaleFormGroupInput): void {
    const saleRawValue = this.convertSaleToSaleRawValue({ ...this.getFormDefaults(), ...sale });
    form.reset(
      {
        ...saleRawValue,
        id: { value: saleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SaleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaVenta: currentTime,
    };
  }

  private convertSaleRawValueToSale(rawSale: SaleFormRawValue | NewSaleFormRawValue): ISale | NewSale {
    return {
      ...rawSale,
      fechaVenta: dayjs(rawSale.fechaVenta, DATE_TIME_FORMAT),
    };
  }

  private convertSaleToSaleRawValue(
    sale: ISale | (Partial<NewSale> & SaleFormDefaults),
  ): SaleFormRawValue | PartialWithRequiredKeyOf<NewSaleFormRawValue> {
    return {
      ...sale,
      fechaVenta: sale.fechaVenta ? sale.fechaVenta.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
