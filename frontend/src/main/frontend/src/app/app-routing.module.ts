import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RegisterComponent} from "./account/register/register.component";
import {LoginComponent} from "./account/login/login.component";
import {ProfileComponent} from "./account/profile/profile.component";
import {RecoveryComponent} from "./account/recovery/recovery.component";
import {AuthGuard} from "./account/auth.guard";
import {FriendsListComponent} from "./account/profile/friends/friends-list.component";
import {SendRecoveryComponent} from "./account/recovery/sendrecovery.component";

const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'profile/details/:login', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'profile/friends', component: FriendsListComponent },
  { path: 'recovery/:token', component: RecoveryComponent },
  { path: 'recovery', component: SendRecoveryComponent },
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
