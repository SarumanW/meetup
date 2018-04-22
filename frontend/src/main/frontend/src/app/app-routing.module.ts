import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegisterComponent} from "./account/register/register.component";
import {LoginComponent} from "./account/login/login.component";
import {RecoveryComponent} from "./account/recovery/recovery.component";
import {AuthGuard} from "./account/auth.guard";
import {HomeComponent} from "./account/home/home.component";
import {SendRecoveryComponent} from "./account/recovery/sendrecovery.component";
import {ContinueRegComponent} from "./account/continueReg/continueReg";

const routes: Routes = [
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'recovery/:token', component: RecoveryComponent},
  {path: 'recovery', component: SendRecoveryComponent},
  {path: 'home/:id', component: HomeComponent},

  //todo uncommit when all will be works
  // { path: 'home/:id', component: HomeComponent, canActivate:[AuthGuard]},
  {path: 'continueReg', component: ContinueRegComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {useHash: true})
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
