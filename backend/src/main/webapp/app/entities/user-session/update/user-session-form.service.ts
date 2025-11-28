import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserSession, NewUserSession } from '../user-session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserSession for edit and NewUserSessionFormGroupInput for create.
 */
type UserSessionFormGroupInput = IUserSession | PartialWithRequiredKeyOf<NewUserSession>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserSession | NewUserSession> = Omit<T, 'updatedAt'> & {
  updatedAt?: string | null;
};

type UserSessionFormRawValue = FormValueOf<IUserSession>;

type NewUserSessionFormRawValue = FormValueOf<NewUserSession>;

type UserSessionFormDefaults = Pick<NewUserSession, 'id' | 'updatedAt'>;

type UserSessionFormGroupContent = {
  id: FormControl<UserSessionFormRawValue['id'] | NewUserSession['id']>;
  userLogin: FormControl<UserSessionFormRawValue['userLogin']>;
  step: FormControl<UserSessionFormRawValue['step']>;
  eventId: FormControl<UserSessionFormRawValue['eventId']>;
  asientos: FormControl<UserSessionFormRawValue['asientos']>;
  nombres: FormControl<UserSessionFormRawValue['nombres']>;
  updatedAt: FormControl<UserSessionFormRawValue['updatedAt']>;
};

export type UserSessionFormGroup = FormGroup<UserSessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserSessionFormService {
  createUserSessionFormGroup(userSession: UserSessionFormGroupInput = { id: null }): UserSessionFormGroup {
    const userSessionRawValue = this.convertUserSessionToUserSessionRawValue({
      ...this.getFormDefaults(),
      ...userSession,
    });
    return new FormGroup<UserSessionFormGroupContent>({
      id: new FormControl(
        { value: userSessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userLogin: new FormControl(userSessionRawValue.userLogin, {
        validators: [Validators.required],
      }),
      step: new FormControl(userSessionRawValue.step, {
        validators: [Validators.required],
      }),
      eventId: new FormControl(userSessionRawValue.eventId),
      asientos: new FormControl(userSessionRawValue.asientos),
      nombres: new FormControl(userSessionRawValue.nombres),
      updatedAt: new FormControl(userSessionRawValue.updatedAt, {
        validators: [Validators.required],
      }),
    });
  }

  getUserSession(form: UserSessionFormGroup): IUserSession | NewUserSession {
    return this.convertUserSessionRawValueToUserSession(form.getRawValue() as UserSessionFormRawValue | NewUserSessionFormRawValue);
  }

  resetForm(form: UserSessionFormGroup, userSession: UserSessionFormGroupInput): void {
    const userSessionRawValue = this.convertUserSessionToUserSessionRawValue({ ...this.getFormDefaults(), ...userSession });
    form.reset(
      {
        ...userSessionRawValue,
        id: { value: userSessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserSessionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      updatedAt: currentTime,
    };
  }

  private convertUserSessionRawValueToUserSession(
    rawUserSession: UserSessionFormRawValue | NewUserSessionFormRawValue,
  ): IUserSession | NewUserSession {
    return {
      ...rawUserSession,
      updatedAt: dayjs(rawUserSession.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertUserSessionToUserSessionRawValue(
    userSession: IUserSession | (Partial<NewUserSession> & UserSessionFormDefaults),
  ): UserSessionFormRawValue | PartialWithRequiredKeyOf<NewUserSessionFormRawValue> {
    return {
      ...userSession,
      updatedAt: userSession.updatedAt ? userSession.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
