import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SeatSelectionResolve from './route/seat-selection-routing-resolve.service';

const seatSelectionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/seat-selection.component').then(m => m.SeatSelectionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/seat-selection-detail.component').then(m => m.SeatSelectionDetailComponent),
    resolve: {
      seatSelection: SeatSelectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/seat-selection-update.component').then(m => m.SeatSelectionUpdateComponent),
    resolve: {
      seatSelection: SeatSelectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/seat-selection-update.component').then(m => m.SeatSelectionUpdateComponent),
    resolve: {
      seatSelection: SeatSelectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default seatSelectionRoute;
