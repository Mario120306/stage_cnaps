import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppGuard } from './modules/general/guard/app.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'system-sop',
    pathMatch: 'full',
  },
  {
    path: 'system-sop',
    //canActivate: [AppGuard],
    loadChildren: () =>
      import('./modules/system-sop/general.module').then((m) => m.GeneralModule),
  },
  {
    path: '**',
    redirectTo: 'system-sop',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
