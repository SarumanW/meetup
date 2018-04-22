import {Component} from '@angular/core';
import {DetailProfile} from "../../detail.profile";
import {AccountService} from "../../account.service";
import {LoginAccount} from "../../login.account";
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'profile-app',
  templateUrl: './profile.component.html'
})

export class ProfileComponent {

  profile: DetailProfile;
  account: LoginAccount;
  states: any;

  constructor(private accountService: AccountService, private http: HttpClient) {
    this.profile = new DetailProfile();
  }

  ngOnInit() {
    this.account = JSON.parse(localStorage.currentUser).id;

    console.log("profile service working");

    this.states = {
      profile: true,
      edit: false,
    };
  }
//  JSON.parse(localStorage.currentUser).id
  getUser(): Observable<any> {
    return this.http.get<any>('api/profile/friends')
      .map(friends => {
        return friends;
      })
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
