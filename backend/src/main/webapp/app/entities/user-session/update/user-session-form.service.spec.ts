import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-session.test-samples';

import { UserSessionFormService } from './user-session-form.service';

describe('UserSession Form Service', () => {
  let service: UserSessionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserSessionFormService);
  });

  describe('Service methods', () => {
    describe('createUserSessionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserSessionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userLogin: expect.any(Object),
            step: expect.any(Object),
            eventId: expect.any(Object),
            asientos: expect.any(Object),
            nombres: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IUserSession should create a new form with FormGroup', () => {
        const formGroup = service.createUserSessionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userLogin: expect.any(Object),
            step: expect.any(Object),
            eventId: expect.any(Object),
            asientos: expect.any(Object),
            nombres: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserSession', () => {
      it('should return NewUserSession for default UserSession initial value', () => {
        const formGroup = service.createUserSessionFormGroup(sampleWithNewData);

        const userSession = service.getUserSession(formGroup) as any;

        expect(userSession).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserSession for empty UserSession initial value', () => {
        const formGroup = service.createUserSessionFormGroup();

        const userSession = service.getUserSession(formGroup) as any;

        expect(userSession).toMatchObject({});
      });

      it('should return IUserSession', () => {
        const formGroup = service.createUserSessionFormGroup(sampleWithRequiredData);

        const userSession = service.getUserSession(formGroup) as any;

        expect(userSession).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserSession should not enable id FormControl', () => {
        const formGroup = service.createUserSessionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserSession should disable id FormControl', () => {
        const formGroup = service.createUserSessionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
