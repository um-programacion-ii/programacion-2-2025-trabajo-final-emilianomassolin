import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserSession } from '../user-session.model';

@Component({
  selector: 'jhi-user-session-detail',
  templateUrl: './user-session-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class UserSessionDetailComponent {
  userSession = input<IUserSession | null>(null);

  previousState(): void {
    window.history.back();
  }
}
