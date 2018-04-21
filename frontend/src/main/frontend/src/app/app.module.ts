import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import { AppComponent } from './app.component';
import {RegisterComponent} from "./account/register/register.component";
import {RecoveryComponent} from "./account/recovery/recovery.component";
import {SendRecoveryComponent} from "./account/recovery/sendrecovery.component";
import {AccountService} from "./account/account.service";
import {LoginComponent} from "./account/login/login.component";
import {ProfileComponent} from "./account/profile/profile.component";
import {AppRoutingModule} from "./app-routing.module";
import {AuthGuard} from "./account/auth.guard";
import {MessageService} from "./account/message.service";
import {FriendsListComponent} from "./account/profile/friends/friends-list.component";
import {FriendComponent} from "./account/profile/friends/friend/friend.component";
import {FriendService} from "./account/profile/friends/friend.service";

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    ProfileComponent,
    RecoveryComponent,
    SendRecoveryComponent
    FriendsListComponent,
    FriendComponent,
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
export class AppModule { }
