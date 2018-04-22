import {Component} from '@angular/core';
import {DetailProfile} from "../detail.profile";
import {AccountService} from "../account.service";
import {LoginAccount} from "../login.account";

@Component({
  selector: 'home-app',
  templateUrl: './home.component.html'
})

export class HomeComponent {

  profile: DetailProfile;
  account: LoginAccount;
  states: any;

  constructor(private accountService: AccountService) {
    this.profile = new DetailProfile();
  }

  ngOnInit() {
    this.account = JSON.parse(localStorage.getItem('currentUser'));

    console.log("profile service working");

    this.states = {
      profile: true,
      events: false,
      friends: false,
      wishList: false,
    };

  }

  switchStates(line: string) {
    for (let laststate in this.states) {
      this.states[laststate] = laststate === line;
    }
  }

  logout() {
    localStorage.clear();
  }
}
