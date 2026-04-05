import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISeatSelection } from '../seat-selection.model';
import { SeatSelectionService } from '../service/seat-selection.service';

@Component({
  templateUrl: './seat-selection-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SeatSelectionDeleteDialogComponent {
  seatSelection?: ISeatSelection;

  protected seatSelectionService = inject(SeatSelectionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.seatSelectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
