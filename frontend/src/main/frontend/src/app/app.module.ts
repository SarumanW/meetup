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
import {FolderListComponent} from "./folders/folder.list/folder.list.component";
import {FolderListService} from "./folders/folder.list.service";
import {FolderComponent} from "./folders/folder/folder.component";
import {FolderService} from "./folders/folder.service";
import { EventComponent } from './events/event/event.component';
import { EventService } from './events/event.service';
import {ModalWindow} from "./modal.window/modal.window.component";
import {HomeComponent} from './account/home/home.component';
import {EditComponent} from "./account/edit/edit.component";
import {FriendComponent} from "./account/friends/friend/friend.component";
import {FriendsListComponent} from "./account/friends/friends-list.component";
import {FriendService} from "./account/friends/friend.service";
import {ContinueRegComponent} from "./account/continueReg/continueReg";

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    ProfileComponent,
    FolderListComponent,
    FolderComponent,
    RecoveryComponent,
    SendRecoveryComponent,
    EventComponent,
    ModalWindow,
    HomeComponent,
    EditComponent,
    FriendsListComponent,
    FriendComponent,
    ContinueRegComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [AccountService,
              AuthGuard,
              FolderListService,
              FolderService,
              EventService,
              FriendService],
  bootstrap: [AppComponent]
})
export class AppModule { }
