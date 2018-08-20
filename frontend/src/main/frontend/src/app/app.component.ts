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

  ngOnInit(){
    console.log("app.component on init")
  }

  constructor(private router : Router, private toastr: ToastrService,){
    this.router.events.subscribe(event => this.modifyHeader(event),
      error => this.showError(error,"Error"));
  }

  logout(){
    localStorage.clear();
    this.router.navigate(["/login"]);
  }

  modifyHeader(location) {
    if (location.url === "/login" || location.url === "/register" || location.url === '/' || location.url === '/continueReg'
    || location.url === "/recovery" || location.url === "/thankyou"
      || (location.url && (location.url.toString().startsWith("/recovery") || location.url.toString().startsWith("/confirmation"))))
    {
      this.showLogout = false;
    } else {
      this.showLogout = true;
    }
  }

  goToProfile(){

    if(localStorage.currentUser){
      this.router.navigate(['/' + this.login() + "/profile"]);
    } else {
      this.router.navigate(['/login']);
    }
  }

  showError(error: any, title: string) {
    let message :string;
    if(error.status === 418) {
      message = 'Please try again later';
      title = 'Server Error'
    } if(error.status === 401){
      message = 'Please login to the system again';
      title = 'Authentication failed'
      this.logout()
    }
    else {
      message = error.error;
    }
      this.toastr.error(message, title, {
        timeOut: 3000,
        positionClass: 'toast-top-right',
        closeButton: true
      });
  }

  login():string{
    return JSON.parse(localStorage.getItem('currentUser')).login;
  }

}
