import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ISeatSelection } from '../seat-selection.model';

@Component({
  selector: 'jhi-seat-selection-detail',
  templateUrl: './seat-selection-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class SeatSelectionDetailComponent {
  seatSelection = input<ISeatSelection | null>(null);

  previousState(): void {
    window.history.back();
  }
}
