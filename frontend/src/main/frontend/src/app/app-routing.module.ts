import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RegisterComponent} from "./account/register/register.component";
import {LoginComponent} from "./account/login/login.component";
import {ProfileComponent} from "./account/profile/profile.component";
import {RecoveryComponent} from "./account/recovery/recovery.component";
import {AuthGuard} from "./account/auth.guard";
import {SendRecoveryComponent} from "./account/recovery/sendrecovery.component";
import {EditComponent} from "./account/edit/edit.component";
import {ContinueRegComponent} from "./account/continueReg/continueReg";

const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'profile/details/:login', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'recovery/:token', component: RecoveryComponent },
  { path: 'recovery', component: SendRecoveryComponent },
  { path: 'edit', component: EditComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'continueReg', component: ContinueRegComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {useHash: true})
  ],
  exports:[
    RouterModule
  ]
})
export class AppRoutingModule { }
