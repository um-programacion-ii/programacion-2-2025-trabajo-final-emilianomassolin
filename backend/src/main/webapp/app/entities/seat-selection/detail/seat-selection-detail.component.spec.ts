import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SeatSelectionDetailComponent } from './seat-selection-detail.component';

describe('SeatSelection Management Detail Component', () => {
  let comp: SeatSelectionDetailComponent;
  let fixture: ComponentFixture<SeatSelectionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeatSelectionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./seat-selection-detail.component').then(m => m.SeatSelectionDetailComponent),
              resolve: { seatSelection: () => of({ id: 6526 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SeatSelectionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SeatSelectionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load seatSelection on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SeatSelectionDetailComponent);

      // THEN
      expect(instance.seatSelection()).toEqual(expect.objectContaining({ id: 6526 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
