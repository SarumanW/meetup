import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import { AppComponent } from './app.component';
import {RegisterComponent} from "./account/register/register.component";
import {AccountService} from "./account/account.service";
import {LoginComponent} from "./account/login/login.component";
import {SuccessComponent} from "./account/success/success.component";
import {AppRoutingModule} from "./app-routing.module";


@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    SuccessComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [AccountService],
  bootstrap: [AppComponent]
})
export class AppModule { }
