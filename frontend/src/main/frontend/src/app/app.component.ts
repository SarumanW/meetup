import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  showLogout : boolean = true;

  ngOnInit(){
    this.router.events.subscribe(event => this.modifyHeader(event));
  }

  constructor(private router : Router){}

  logout(){
    localStorage.clear();
    this.router.navigate(["/login"]);
  }

  modifyHeader(location) {
    if (location.url === "/login" || location.url === "/register" || location.url === '/' || location.url === '/continueReg')
    {
      this.showLogout = false;
    } else {
      this.showLogout = true;
    }
  }

  goToProfile(){
    this.router.navigate(['/profile/'+JSON.parse(localStorage.currentUser).id]);
  }

}
