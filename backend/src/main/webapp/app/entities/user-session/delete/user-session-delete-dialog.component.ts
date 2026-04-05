import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserSession } from '../user-session.model';
import { UserSessionService } from '../service/user-session.service';

@Component({
  templateUrl: './user-session-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserSessionDeleteDialogComponent {
  userSession?: IUserSession;

  protected userSessionService = inject(UserSessionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userSessionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
