import { Component } from '@angular/core';
import {DetailProfile} from "../detail.profile";
import {AccountService} from "../account.service";
import {LoginAccount} from "../login.account";

@Component({
  selector: 'app-success',
  templateUrl: './profile.component.html'
})

export class ProfileComponent {

  profile : DetailProfile;
  account : LoginAccount;

  constructor(private accountService: AccountService){
    this.profile = new DetailProfile();
  }

  ngOnInit() {
    this.account = JSON.parse(localStorage.getItem('currentUser'));

    console.log("profile service working");

    this.accountService.profile(this.account).subscribe(
      (data) => {
        this.profile = data;
      }
    )
  }

  logout(){
    localStorage.clear();
  }
}
