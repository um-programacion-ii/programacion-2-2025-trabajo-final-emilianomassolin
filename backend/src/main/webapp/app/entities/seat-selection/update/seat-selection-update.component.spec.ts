import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SeatSelectionService } from '../service/seat-selection.service';
import { ISeatSelection } from '../seat-selection.model';
import { SeatSelectionFormService } from './seat-selection-form.service';

import { SeatSelectionUpdateComponent } from './seat-selection-update.component';

describe('SeatSelection Management Update Component', () => {
  let comp: SeatSelectionUpdateComponent;
  let fixture: ComponentFixture<SeatSelectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let seatSelectionFormService: SeatSelectionFormService;
  let seatSelectionService: SeatSelectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SeatSelectionUpdateComponent],
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
      .overrideTemplate(SeatSelectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SeatSelectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    seatSelectionFormService = TestBed.inject(SeatSelectionFormService);
    seatSelectionService = TestBed.inject(SeatSelectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const seatSelection: ISeatSelection = { id: 8295 };

      activatedRoute.data = of({ seatSelection });
      comp.ngOnInit();

      expect(comp.seatSelection).toEqual(seatSelection);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeatSelection>>();
      const seatSelection = { id: 6526 };
      jest.spyOn(seatSelectionFormService, 'getSeatSelection').mockReturnValue(seatSelection);
      jest.spyOn(seatSelectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seatSelection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seatSelection }));
      saveSubject.complete();

      // THEN
      expect(seatSelectionFormService.getSeatSelection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(seatSelectionService.update).toHaveBeenCalledWith(expect.objectContaining(seatSelection));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeatSelection>>();
      const seatSelection = { id: 6526 };
      jest.spyOn(seatSelectionFormService, 'getSeatSelection').mockReturnValue({ id: null });
      jest.spyOn(seatSelectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seatSelection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seatSelection }));
      saveSubject.complete();

      // THEN
      expect(seatSelectionFormService.getSeatSelection).toHaveBeenCalled();
      expect(seatSelectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeatSelection>>();
      const seatSelection = { id: 6526 };
      jest.spyOn(seatSelectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seatSelection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(seatSelectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
