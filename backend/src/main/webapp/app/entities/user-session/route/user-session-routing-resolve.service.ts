import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserSession } from '../user-session.model';
import { UserSessionService } from '../service/user-session.service';

const userSessionResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserSession> => {
  const id = route.params.id;
  if (id) {
    return inject(UserSessionService)
      .find(id)
      .pipe(
        mergeMap((userSession: HttpResponse<IUserSession>) => {
          if (userSession.body) {
            return of(userSession.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userSessionResolve;
