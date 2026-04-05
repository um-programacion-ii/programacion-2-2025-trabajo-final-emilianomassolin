import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { UserSessionService } from '../service/user-session.service';
import { IUserSession } from '../user-session.model';
import { UserSessionFormService } from './user-session-form.service';

import { UserSessionUpdateComponent } from './user-session-update.component';

describe('UserSession Management Update Component', () => {
  let comp: UserSessionUpdateComponent;
  let fixture: ComponentFixture<UserSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userSessionFormService: UserSessionFormService;
  let userSessionService: UserSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserSessionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UserSessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserSessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userSessionFormService = TestBed.inject(UserSessionFormService);
    userSessionService = TestBed.inject(UserSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const userSession: IUserSession = { id: 23031 };

      activatedRoute.data = of({ userSession });
      comp.ngOnInit();

      expect(comp.userSession).toEqual(userSession);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSession>>();
      const userSession = { id: 31358 };
      jest.spyOn(userSessionFormService, 'getUserSession').mockReturnValue(userSession);
      jest.spyOn(userSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userSession }));
      saveSubject.complete();

      // THEN
      expect(userSessionFormService.getUserSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userSessionService.update).toHaveBeenCalledWith(expect.objectContaining(userSession));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSession>>();
      const userSession = { id: 31358 };
      jest.spyOn(userSessionFormService, 'getUserSession').mockReturnValue({ id: null });
      jest.spyOn(userSessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userSession }));
      saveSubject.complete();

      // THEN
      expect(userSessionFormService.getUserSession).toHaveBeenCalled();
      expect(userSessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSession>>();
      const userSession = { id: 31358 };
      jest.spyOn(userSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userSessionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
