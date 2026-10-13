import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GeneralRoutingModule } from './general-routing.module';
import { GeneralComponent } from './general.component';
import { DocumentationComponent } from './pages/documentation/documentation.component';
import { PageVideComponent } from './pages/page-vide/page-vide.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';

@NgModule({
  declarations: [
    GeneralComponent,
    DocumentationComponent,
    PageVideComponent,
    FooterComponent,
    NavbarComponent,
    SidebarComponent,
  ],
  imports: [CommonModule, GeneralRoutingModule],
  exports: [FooterComponent, NavbarComponent, SidebarComponent],
})
export class GeneralModule {}
