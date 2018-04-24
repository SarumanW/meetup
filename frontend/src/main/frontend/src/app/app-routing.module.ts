import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegisterComponent} from "./account/register/register.component";
import {LoginComponent} from "./account/login/login.component";
import {ProfileComponent} from "./account/profile/profile.component";
import {RecoveryComponent} from "./account/recovery/recovery.component";
import {AuthGuard} from "./account/auth.guard";
import {FolderListComponent} from "./folders/folder.list/folder.list.component";
import {FolderComponent} from "./folders/folder/folder.component";
import {SendRecoveryComponent} from "./account/recovery/sendrecovery.component";
import {EventComponent} from "./events/event/event.component";
import {EditComponent} from "./account/edit/edit.component";
import {FriendsListComponent} from "./account/friends/friends.list.component";
import {ContinueRegistrationComponent} from "./account/continue.registration/continue.registration.component";
import {ChangePasswordComponent} from "./account/change.password/change.password.component";

const routes: Routes = [
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  // { path: 'profile/:id', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'profile/:id', component: ProfileComponent},
  {path: 'profile/:id/folders', component: FolderListComponent},
  {path: 'profile/:id/folders/:folderId', component: FolderComponent},
  {path: 'recovery/:token', component: RecoveryComponent},
  {path: 'recovery', component: SendRecoveryComponent},
  {path: 'profile/:id/folders/:folderId/events/:eventId', component: EventComponent},
  {path: 'edit/:id', component: EditComponent},
  {path: 'friends', component: FriendsListComponent},
  {path: 'continue.registration', component: ContinueRegistrationComponent},
  {path: 'folders', component: FolderListComponent},
  {path: 'change.password', component: ChangePasswordComponent},

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
