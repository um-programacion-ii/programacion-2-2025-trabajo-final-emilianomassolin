import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISeatSelection } from '../seat-selection.model';
import { SeatSelectionService } from '../service/seat-selection.service';
import { SeatSelectionFormGroup, SeatSelectionFormService } from './seat-selection-form.service';

@Component({
  selector: 'jhi-seat-selection-update',
  templateUrl: './seat-selection-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SeatSelectionUpdateComponent implements OnInit {
  isSaving = false;
  seatSelection: ISeatSelection | null = null;

  protected seatSelectionService = inject(SeatSelectionService);
  protected seatSelectionFormService = inject(SeatSelectionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SeatSelectionFormGroup = this.seatSelectionFormService.createSeatSelectionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seatSelection }) => {
      this.seatSelection = seatSelection;
      if (seatSelection) {
        this.updateForm(seatSelection);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const seatSelection = this.seatSelectionFormService.getSeatSelection(this.editForm);
    if (seatSelection.id !== null) {
      this.subscribeToSaveResponse(this.seatSelectionService.update(seatSelection));
    } else {
      this.subscribeToSaveResponse(this.seatSelectionService.create(seatSelection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeatSelection>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(seatSelection: ISeatSelection): void {
    this.seatSelection = seatSelection;
    this.seatSelectionFormService.resetForm(this.editForm, seatSelection);
  }
}
