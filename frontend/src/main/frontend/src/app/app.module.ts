import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import {AppComponent} from './app.component';
import {RegisterComponent} from "./account/register/register.component";
import {RecoveryComponent} from "./account/recovery/recovery.component";
import {SendRecoveryComponent} from "./account/recovery/sendrecovery.component";
import {AccountService} from "./account/account.service";
import {LoginComponent} from "./account/login/login.component";
import {AppRoutingModule} from "./app-routing.module";
import {AuthGuard} from "./account/auth.guard";
import {MessageService} from "./account/message.service";
import {HomeComponent} from "./account/home/home.component";
import {ProfileComponent} from "./account/home/profile/profile.component";
import {EditComponent} from "./account/home/edit/edit.component";
import {ContinueRegComponent} from "./account/continueReg/continueReg"
import {FriendsListComponent} from "./account/home/friends/friends-list.component";
import {FriendComponent} from "./account/home/friends/friend/friend.component";
import {FriendService} from "./account/home/friends/friend.service";

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    ProfileComponent,
    EditComponent,
    RecoveryComponent,
    SendRecoveryComponent,
    HomeComponent,
    ProfileComponent,
    SendRecoveryComponent,
    ContinueRegComponent,
    SendRecoveryComponent,
    FriendsListComponent,
    FriendComponent,
    SendRecoveryComponent,
    ContinueRegComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [AccountService,
    AuthGuard,
    MessageService,
    FriendService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
