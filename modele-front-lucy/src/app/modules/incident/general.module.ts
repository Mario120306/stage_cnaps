import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GeneralRoutingModule } from './general-routing.module';
import { GeneralComponent } from './general.component';
import { DocumentationComponent } from './pages/documentation/documentation.component';
import { PageVideComponent } from './pages/page-vide/page-vide.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { IncidentDashboardComponent } from './pages/incident-dashboard/incident-dashboard.component';
import { IncidentDashComponent } from './pages/incidentDash/incident-dash.component';
import { IncidentFormComponent } from './pages/incident-form/incident-form.component';
import { Pipe, PipeTransform } from '@angular/core';
import { IncidentDetailComponent } from './pages/incident-detail/incident-detail.component';
import { IncidentSolutionComponent } from './pages/incident-solution/incident-form.component';
import { IncidentListComponent } from './pages/incident-list/incident-list.component';
import { IncidentDetaileComponent } from './pages/incident-list/incident-detail.component';

@NgModule({
  declarations: [
    GeneralComponent,
    DocumentationComponent,
    PageVideComponent,
    FooterComponent,
    NavbarComponent,
    SidebarComponent,
    DashboardComponent,
    IncidentDashboardComponent,
    IncidentDashComponent,
    IncidentFormComponent,
    IncidentDetailComponent,
    IncidentSolutionComponent,
    IncidentListComponent,
    IncidentDetaileComponent
  ],
  imports: [CommonModule,FormsModule, GeneralRoutingModule],
  exports: [FooterComponent, NavbarComponent, SidebarComponent],
})
// @Pipe({name: 'filterByCategory'})
// export class FilterByCategoryPipe implements PipeTransform {
//   transform(types: any[], categorie: any): any[] {
//     if (!categorie || !types) return types;
//     return types.filter(type => type.categorieId === categorie.id);
//   }
// }
export class GeneralModule {}
