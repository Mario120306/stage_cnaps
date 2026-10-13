import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GeneralComponent } from './general.component';
import { DocumentationComponent } from './pages/documentation/documentation.component';
import { PageVideComponent } from './pages/page-vide/page-vide.component';
import { InsertionComponent } from './pages/insertion/insertion.component';
import { ListeComponent } from './pages/liste/liste.component';
import { SolutionComponent } from './pages/solution/solution.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { IncidentDashboardComponent } from './pages/incident-dashboard/incident-dashboard.component';
import { IncidentDashComponent } from './pages/incidentDash/incident-dash.component';
import { IncidentFormComponent } from './pages/incident-form/incident-form.component';
import { IncidentDetailComponent } from './pages/incident-detail/incident-detail.component';
import { IncidentSolutionComponent } from './pages/incident-solution/incident-form.component';
import { IncidentListComponent } from './pages/incident-list/incident-list.component';
import { IncidentDetaileComponent } from './pages/incident-list/incident-detail.component';

const routes: Routes = [
  {
    path: '',
    component: GeneralComponent,
    children: [
      {
        path: '',
        redirectTo: 'documentation',
        pathMatch: 'full',
      },
      {
        path: 'documentation', // child route path
        component: DocumentationComponent, // child route component that the router renders
      },
      {
        path: 'incident-dash', // child route path
        component: IncidentDashComponent, // child route component that the router renders
      },
      {
        path: 'incident-detail/:id', // child route path
        component: IncidentDetailComponent, // child route component that the router renders
      },
      {
        path: 'incident-form', // child route path
        component: IncidentFormComponent, // child route component that the router renders
      },
      { path: 'incidents', component: IncidentListComponent },
      { path: 'incidents/:id', component: IncidentDetaileComponent },
      {
        path: 'dashboard', // child route path
        component: DashboardComponent, // child route component that the router renders
      },
      {
        path: 'incident-solution/:id', // child route path
        component: IncidentSolutionComponent, // child route component that the router renders
      },
      {
        path: 'indicent-dashboard', // child route path
        component: IncidentDashboardComponent, // child route component that the router renders
      },
      {
        path: 'page-vide', // child route path
        component: PageVideComponent, // child route component that the router renders
      },
      {
        path: 'insertion', // child route path
        component: InsertionComponent, // child route component that the router renders
      },
      {
        path: 'liste', // child route path
        component: ListeComponent, // child route component that the router renders
      },
      {
        path: 'solution', // child route path
        component: SolutionComponent, // child route component that the router renders
      },
      { path: '**', redirectTo: 'documentation' },
    ],
  },
  { path: '**', redirectTo: 'documentation' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GeneralRoutingModule {}
