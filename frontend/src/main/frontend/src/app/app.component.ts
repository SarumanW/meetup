import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {Profile} from "./account/profile";
import {ToastrService} from "ngx-toastr";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {

  showLogout : boolean = true;
  profile : Profile;


  ngOnInit(){
    this.router.events.subscribe(event => this.modifyHeader(event));
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }

  constructor(private router : Router, private toastr: ToastrService,){}

  logout(){
    localStorage.clear();
    this.router.navigate(["/login"]);
  }

  modifyHeader(location) {
    if (location.url === "/login" || location.url === "/register" || location.url === '/' || location.url === '/continueReg'
    || location.url === "/recovery" || location.url === "/thankyou")
    {
      this.showLogout = false;
    } else {
      this.showLogout = true;
    }
  }

  goToProfile(){

    if(localStorage.currentUser){
      this.router.navigate(['/' + this.profile.login + "/profile"]);
    } else {
      this.router.navigate(['/login']);
    }
  }

  showError(error: any, title: string) {
    let message :string;
    if(error.status === 418) {
      message = 'Please try again later';
      title = 'Server Error'
    } else {
      message = error.error;
    }
      this.toastr.error(message, title, {
        timeOut: 3000,
        positionClass: 'toast-top-right',
        closeButton: true
      });
  }

  login():string{
    return this.profile.login;
  }

}
