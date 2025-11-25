import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISeatSelection } from '../seat-selection.model';
import { SeatSelectionService } from '../service/seat-selection.service';

const seatSelectionResolve = (route: ActivatedRouteSnapshot): Observable<null | ISeatSelection> => {
  const id = route.params.id;
  if (id) {
    return inject(SeatSelectionService)
      .find(id)
      .pipe(
        mergeMap((seatSelection: HttpResponse<ISeatSelection>) => {
          if (seatSelection.body) {
            return of(seatSelection.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default seatSelectionResolve;
