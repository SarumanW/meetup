import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {Profile} from "./account/profile";


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

  constructor(private router : Router){}

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

  login():string{
    return this.profile.login;
  }

}
