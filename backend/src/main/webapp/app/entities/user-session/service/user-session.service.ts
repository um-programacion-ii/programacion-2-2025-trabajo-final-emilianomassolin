import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserSession, NewUserSession } from '../user-session.model';

export type PartialUpdateUserSession = Partial<IUserSession> & Pick<IUserSession, 'id'>;

type RestOf<T extends IUserSession | NewUserSession> = Omit<T, 'updatedAt'> & {
  updatedAt?: string | null;
};

export type RestUserSession = RestOf<IUserSession>;

export type NewRestUserSession = RestOf<NewUserSession>;

export type PartialUpdateRestUserSession = RestOf<PartialUpdateUserSession>;

export type EntityResponseType = HttpResponse<IUserSession>;
export type EntityArrayResponseType = HttpResponse<IUserSession[]>;

@Injectable({ providedIn: 'root' })
export class UserSessionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-sessions');

  create(userSession: NewUserSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSession);
    return this.http
      .post<RestUserSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userSession: IUserSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSession);
    return this.http
      .put<RestUserSession>(`${this.resourceUrl}/${this.getUserSessionIdentifier(userSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userSession: PartialUpdateUserSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSession);
    return this.http
      .patch<RestUserSession>(`${this.resourceUrl}/${this.getUserSessionIdentifier(userSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserSessionIdentifier(userSession: Pick<IUserSession, 'id'>): number {
    return userSession.id;
  }

  compareUserSession(o1: Pick<IUserSession, 'id'> | null, o2: Pick<IUserSession, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserSessionIdentifier(o1) === this.getUserSessionIdentifier(o2) : o1 === o2;
  }

  addUserSessionToCollectionIfMissing<Type extends Pick<IUserSession, 'id'>>(
    userSessionCollection: Type[],
    ...userSessionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userSessions: Type[] = userSessionsToCheck.filter(isPresent);
    if (userSessions.length > 0) {
      const userSessionCollectionIdentifiers = userSessionCollection.map(userSessionItem => this.getUserSessionIdentifier(userSessionItem));
      const userSessionsToAdd = userSessions.filter(userSessionItem => {
        const userSessionIdentifier = this.getUserSessionIdentifier(userSessionItem);
        if (userSessionCollectionIdentifiers.includes(userSessionIdentifier)) {
          return false;
        }
        userSessionCollectionIdentifiers.push(userSessionIdentifier);
        return true;
      });
      return [...userSessionsToAdd, ...userSessionCollection];
    }
    return userSessionCollection;
  }

  protected convertDateFromClient<T extends IUserSession | NewUserSession | PartialUpdateUserSession>(userSession: T): RestOf<T> {
    return {
      ...userSession,
      updatedAt: userSession.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserSession: RestUserSession): IUserSession {
    return {
      ...restUserSession,
      updatedAt: restUserSession.updatedAt ? dayjs(restUserSession.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserSession>): HttpResponse<IUserSession> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserSession[]>): HttpResponse<IUserSession[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
