import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import {AppComponent} from './app.component';
import {RegisterComponent} from "./account/register/register.component";
import {RecoveryComponent} from "./account/recovery/recovery.component";
import {SendRecoveryComponent} from "./account/recovery/send.recovery.component";
import {AccountService} from "./account/account.service";
import {LoginComponent} from "./account/login/login.component";
import {ProfileComponent} from "./account/profile/profile.component";
import {AppRoutingModule} from "./app-routing.module";
import {AuthGuard} from "./account/auth.guard";
import {FolderListComponent} from "./folders/folder.list/folder.list.component";
import {FolderListService} from "./folders/folder.list.service";
import {FolderComponent} from "./folders/folder/folder.component";
import {EventComponent} from './events/event/event.component';
import {EventService} from './events/event.service';
import {ModalWindow} from "./modal.window/modal.window.component";
import {HomeComponent} from './account/home/home.component';
import {EditComponent} from "./account/edit/edit.component";
import {FriendComponent} from "./account/friends/friend/friend.component";
import {FriendsListComponent} from "./account/friends/friends.list.component";
import {FriendService} from "./account/friends/friend.service";
import {ChangePasswordComponent} from "./account/change.password/changePassword.component";
import {UploadFileService} from "./upload.file/upload.file.service";
import {ToastrModule} from "ngx-toastr";
import {ThankyouComponent} from "./account/thankyou/thankyou.component";
import {NgxSpinnerModule} from 'ngx-spinner';
import {EventListComponent} from "./events/event.list/event.list.component";
import {Ng2TableModule} from "ng2-table";
import {TooltipModule, PaginationModule, BsDropdownModule} from "ngx-bootstrap";
import {PopupModule} from "ng2-opd-popup";
import {EventAddComponent} from './events/event.add/event.add.component';
import {CalendarModule} from "angular-calendar";
import {CalendarComponent} from "./calendar/calendar.component/calendar.component";
import {UtilsModule} from "./calendar/calendar.utils/utils.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CalendarService} from "./calendar/calendar.service";
import {ImageUploadService} from "./events/image.upload.service";
import {AgmCoreModule} from "@agm/core";
import {environment} from "./environment";
import { TextMaskModule } from 'angular2-text-mask';
import {CheckPasswordComponent} from "./account/change.password/check.password/checkPassword.component"
import {EventEditComponent} from "./events/event.edit/event.edit.component";
import { ChatComponent } from './chat/chat/chat.component';
import {ChatService} from "./chat/chat.service";
import { WishListComponent } from './wishes/wish.list/wish.list.component';
import { WishComponent } from './wishes/wish/wish.component';
import {WishListService} from "./wishes/wish.list.service";
import { WishAddComponent } from './wishes/wish.add/wish.add.component';
import {WishService} from "./wishes/wish.service";
import { WishEditComponent } from './wishes/wish.edit/wish.edit.component';
import {CountDown} from "./account/countdown/countdown";
import { CommentListComponent } from './wishes/wish/comment-list/comment-list.component';
import {CommentService} from "./wishes/wish/comment-list/comment.service";
import {ConfirmationComponent} from "./account/confirmation/confirmation.component";

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
    ChangePasswordComponent,
    ThankyouComponent,
    EventListComponent,
    EventAddComponent,
    CalendarComponent,
    EventEditComponent,
    ChatComponent,
    WishListComponent,
    WishComponent,
    WishAddComponent,
    EventEditComponent,
    CheckPasswordComponent,
    WishEditComponent,
    CountDown,
    CommentListComponent,
    ConfirmationComponent,
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    ToastrModule.forRoot(),
    NgbModule.forRoot(),
    NgxSpinnerModule,
    TooltipModule.forRoot(),
    Ng2TableModule,
    PaginationModule.forRoot(),
    PopupModule.forRoot(),
    CalendarModule.forRoot(),
    UtilsModule,
    BrowserAnimationsModule,
    TextMaskModule,
    AgmCoreModule.forRoot({
      apiKey: environment.googleMapsApiKey,
      libraries: ["places"]
    }),
    BsDropdownModule.forRoot(),
  ],
  providers: [AccountService,
    AuthGuard,
    FolderListService,
    EventService,
    FriendService,
    UploadFileService,
    CalendarService,
    ImageUploadService,
    ChatService,
    WishListService,
    WishService,
    CommentService
  ],
  bootstrap: [AppComponent]
})

export class AppModule {
}
