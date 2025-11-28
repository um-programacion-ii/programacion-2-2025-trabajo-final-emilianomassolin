import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISeatSelection, NewSeatSelection } from '../seat-selection.model';

export type PartialUpdateSeatSelection = Partial<ISeatSelection> & Pick<ISeatSelection, 'id'>;

type RestOf<T extends ISeatSelection | NewSeatSelection> = Omit<T, 'fechaSeleccion' | 'expiracion'> & {
  fechaSeleccion?: string | null;
  expiracion?: string | null;
};

export type RestSeatSelection = RestOf<ISeatSelection>;

export type NewRestSeatSelection = RestOf<NewSeatSelection>;

export type PartialUpdateRestSeatSelection = RestOf<PartialUpdateSeatSelection>;

export type EntityResponseType = HttpResponse<ISeatSelection>;
export type EntityArrayResponseType = HttpResponse<ISeatSelection[]>;

@Injectable({ providedIn: 'root' })
export class SeatSelectionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/seat-selections');

  create(seatSelection: NewSeatSelection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seatSelection);
    return this.http
      .post<RestSeatSelection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(seatSelection: ISeatSelection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seatSelection);
    return this.http
      .put<RestSeatSelection>(`${this.resourceUrl}/${this.getSeatSelectionIdentifier(seatSelection)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(seatSelection: PartialUpdateSeatSelection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seatSelection);
    return this.http
      .patch<RestSeatSelection>(`${this.resourceUrl}/${this.getSeatSelectionIdentifier(seatSelection)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSeatSelection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSeatSelection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSeatSelectionIdentifier(seatSelection: Pick<ISeatSelection, 'id'>): number {
    return seatSelection.id;
  }

  compareSeatSelection(o1: Pick<ISeatSelection, 'id'> | null, o2: Pick<ISeatSelection, 'id'> | null): boolean {
    return o1 && o2 ? this.getSeatSelectionIdentifier(o1) === this.getSeatSelectionIdentifier(o2) : o1 === o2;
  }

  addSeatSelectionToCollectionIfMissing<Type extends Pick<ISeatSelection, 'id'>>(
    seatSelectionCollection: Type[],
    ...seatSelectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const seatSelections: Type[] = seatSelectionsToCheck.filter(isPresent);
    if (seatSelections.length > 0) {
      const seatSelectionCollectionIdentifiers = seatSelectionCollection.map(seatSelectionItem =>
        this.getSeatSelectionIdentifier(seatSelectionItem),
      );
      const seatSelectionsToAdd = seatSelections.filter(seatSelectionItem => {
        const seatSelectionIdentifier = this.getSeatSelectionIdentifier(seatSelectionItem);
        if (seatSelectionCollectionIdentifiers.includes(seatSelectionIdentifier)) {
          return false;
        }
        seatSelectionCollectionIdentifiers.push(seatSelectionIdentifier);
        return true;
      });
      return [...seatSelectionsToAdd, ...seatSelectionCollection];
    }
    return seatSelectionCollection;
  }

  protected convertDateFromClient<T extends ISeatSelection | NewSeatSelection | PartialUpdateSeatSelection>(seatSelection: T): RestOf<T> {
    return {
      ...seatSelection,
      fechaSeleccion: seatSelection.fechaSeleccion?.toJSON() ?? null,
      expiracion: seatSelection.expiracion?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSeatSelection: RestSeatSelection): ISeatSelection {
    return {
      ...restSeatSelection,
      fechaSeleccion: restSeatSelection.fechaSeleccion ? dayjs(restSeatSelection.fechaSeleccion) : undefined,
      expiracion: restSeatSelection.expiracion ? dayjs(restSeatSelection.expiracion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSeatSelection>): HttpResponse<ISeatSelection> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSeatSelection[]>): HttpResponse<ISeatSelection[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
