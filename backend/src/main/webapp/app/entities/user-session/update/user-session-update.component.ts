import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserSession } from '../user-session.model';
import { UserSessionService } from '../service/user-session.service';
import { UserSessionFormGroup, UserSessionFormService } from './user-session-form.service';

@Component({
  selector: 'jhi-user-session-update',
  templateUrl: './user-session-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserSessionUpdateComponent implements OnInit {
  isSaving = false;
  userSession: IUserSession | null = null;

  protected userSessionService = inject(UserSessionService);
  protected userSessionFormService = inject(UserSessionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserSessionFormGroup = this.userSessionFormService.createUserSessionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userSession }) => {
      this.userSession = userSession;
      if (userSession) {
        this.updateForm(userSession);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userSession = this.userSessionFormService.getUserSession(this.editForm);
    if (userSession.id !== null) {
      this.subscribeToSaveResponse(this.userSessionService.update(userSession));
    } else {
      this.subscribeToSaveResponse(this.userSessionService.create(userSession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserSession>>): void {
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

  protected updateForm(userSession: IUserSession): void {
    this.userSession = userSession;
    this.userSessionFormService.resetForm(this.editForm, userSession);
  }
}
