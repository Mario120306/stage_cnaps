import { RouterModule, Routes } from "@angular/router";
import { GeneralComponent } from "./general.component";
import { ListeSopComponent } from "./pages/liste-sop/liste-sop.component";
import { NgModule } from "@angular/core";
import { CreationSopComponent } from "./pages/creation-sop/creation-sop.component";
import { VueSopComponent } from "./pages/vue-sop/vue-sop.component";
import { ModificationSopComponent } from "./pages/modification-sop/modification-sop.component";

const routes:Routes=[
    {
        path:'',
        component:GeneralComponent,
        children:[
            {
                path:'',
                redirectTo:'liste-sop',
                pathMatch:'full',
            },
            {
                path:'liste-sop',
                component: ListeSopComponent,
            },
            {
                path:'creation-sop',
                component:CreationSopComponent,
            }
            ,
            {
                path:'vue-sop',
                component:VueSopComponent,
            },
            {
                path:'modification-sop',
                component:ModificationSopComponent,
            }
        ],
    },
    {path:'**',redirectTo:'liste-sop'},
];

@NgModule({
    imports:[RouterModule.forChild(routes)],
    exports:[RouterModule],
})
export class GeneralRoutingModule{}