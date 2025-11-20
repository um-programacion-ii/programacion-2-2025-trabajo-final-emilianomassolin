import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'backendApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'event',
    data: { pageTitle: 'backendApp.event.home.title' },
    loadChildren: () => import('./event/event.routes'),
  },
  {
    path: 'sale',
    data: { pageTitle: 'backendApp.sale.home.title' },
    loadChildren: () => import('./sale/sale.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
