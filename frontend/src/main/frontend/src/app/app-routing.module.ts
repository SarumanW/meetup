import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RegisterComponent} from "./account/register/register.component";
import {LoginComponent} from "./account/login/login.component";
import {ProfileComponent} from "./account/profile/profile.component";
import {AuthGuard} from "./account/auth.guard";
import {FolderListComponent} from "./folders/folder.list/folder.list.component";
import {FolderComponent} from "./folders/folder/folder.component";

const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'profile/:id', component: ProfileComponent, canActivate: [AuthGuard]},
  { path: 'profile/:id/folders', component: FolderListComponent}, //try with 'profile/:id/folders'
  { path: 'profile/:id/folders/:folderId', component: FolderComponent} //delete later
  // { path: 'profile/details/:login', component: ProfileComponent, canActivate: [AuthGuard] },
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
