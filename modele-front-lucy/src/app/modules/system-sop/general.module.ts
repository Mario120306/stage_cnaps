import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { GeneralRoutingModule } from './general-routing.module';
import { GeneralComponent } from './general.component';
import { ListeSopComponent } from './pages/liste-sop/liste-sop.component';
import { CreationSopComponent } from './pages/creation-sop/creation-sop.component';
import { VueSopComponent } from './pages/vue-sop/vue-sop.component';

@NgModule({
	declarations: [GeneralComponent, ListeSopComponent, CreationSopComponent, VueSopComponent],
	imports: [CommonModule, FormsModule, GeneralRoutingModule],
})
export class GeneralModule {}
