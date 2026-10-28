import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { GeneralRoutingModule } from './general-routing.module';
import { GeneralComponent } from './general.component';
import { ListeSopComponent } from './pages/liste-sop/liste-sop.component';
import { CreationSopComponent } from './pages/creation-sop/creation-sop.component';
import { VueSopComponent } from './pages/vue-sop/vue-sop.component';
import { ModificationSopComponent } from './pages/modification-sop/modification-sop.component';

@NgModule({
	declarations: [GeneralComponent, ListeSopComponent, CreationSopComponent, VueSopComponent, ModificationSopComponent],
	imports: [CommonModule, FormsModule, ReactiveFormsModule, GeneralRoutingModule],
})
export class GeneralModule {}
