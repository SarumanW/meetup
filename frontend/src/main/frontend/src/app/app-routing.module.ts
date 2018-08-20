import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegisterComponent} from "./account/register/register.component";
import {LoginComponent} from "./account/login/login.component";
import {ProfileComponent} from "./account/profile/profile.component";
import {RecoveryComponent} from "./account/recovery/recovery.component";
import {AuthGuard} from "./account/auth.guard";
import {FolderListComponent} from "./folders/folder.list/folder.list.component";
import {FolderComponent} from "./folders/folder/folder.component";
import {SendRecoveryComponent} from "./account/recovery/send.recovery.component";
import {EventComponent} from "./events/event/event.component";
import {EditComponent} from "./account/edit/edit.component";
import {FriendsListComponent} from "./account/friends/friends.list.component";
import {ChangePasswordComponent} from "./account/change.password/changePassword.component";
import {ThankyouComponent} from "./account/thankyou/thankyou.component";
import {EventListComponent} from "./events/event.list/event.list.component";
import {EventAddComponent} from "./events/event.add/event.add.component";
import {CalendarComponent} from "./calendar/calendar.component/calendar.component";
import {WishListComponent} from "./wishes/wish.list/wish.list.component";
import {WishComponent} from "./wishes/wish/wish.component";
import {WishAddComponent} from "./wishes/wish.add/wish.add.component";
import {EventEditComponent} from "./events/event.edit/event.edit.component";
import {ChatComponent} from "./chat/chat/chat.component";
import {CheckPasswordComponent} from "./account/change.password/check.password/checkPassword.component"
import {WishEditComponent} from "./wishes/wish.edit/wish.edit.component";
import {ConfirmationComponent} from "./account/confirmation/confirmation.component";

const routes: Routes = [
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: ':login/profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'recovery/:token', component: RecoveryComponent},
  {path: 'confirmation/:token', component: ConfirmationComponent},
  {path: 'recovery', component: SendRecoveryComponent},
  {path: ':login/edit', component: EditComponent, canActivate: [AuthGuard]},
  {path: ':login/friends', component: FriendsListComponent, canActivate: [AuthGuard]},
  {path: ':login/folders', component: FolderListComponent, canActivate: [AuthGuard]},
  {path: ':login/calendar', component: CalendarComponent, canActivate: [AuthGuard]},
  {path: ':login/folders/:folderId', component: FolderComponent, canActivate: [AuthGuard]},
  {path: ':login/folders/:folderId/:type', component: EventListComponent, canActivate: [AuthGuard]},
  {path: ':login/folders/:folderId/:type/:eventId', component: EventComponent, canActivate: [AuthGuard]},
  {path: ':login/change.password', component: ChangePasswordComponent, canActivate: [AuthGuard]},
  {path: ':login/check.password', component: CheckPasswordComponent, canActivate: [AuthGuard]},
  {path: ':login/wishes', component: WishListComponent, canActivate: [AuthGuard]},
  {path: ':login/wishes/category/:category', component: WishListComponent, canActivate: [AuthGuard]},
  {path: ':login/wishes/category/:category/:tag', component: WishListComponent, canActivate: [AuthGuard]},
  {path: ':login/wishes/add', component: WishAddComponent, canActivate: [AuthGuard]},
  {path: ':login/wishes/edit/:itemId', component: WishEditComponent, canActivate: [AuthGuard]},
  {path: ':login/wishes/:itemId', component: WishComponent, canActivate: [AuthGuard]},
  {path: 'change.password', component: ChangePasswordComponent},
  {path: 'thankyou', component: ThankyouComponent},
  {path: ':login/event/add/:folderId', component: EventAddComponent, canActivate: [AuthGuard] },
  {path: ':login/folders/:folderId/:type/:eventId/edit', component: EventEditComponent, canActivate: [AuthGuard]},
  {path: ':login/folders/:folderId/event/:eventId/chat/:chatId', component: ChatComponent, canActivate: [AuthGuard]},
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
