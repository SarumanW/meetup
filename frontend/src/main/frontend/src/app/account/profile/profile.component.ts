import {Component} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {Router} from "@angular/router";

@Component({
 templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent {

  state:string="profile";
  profile: Profile;


  constructor(private accountService: AccountService,
              private router: Router) {
    this.profile = new Profile();
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));

    console.log("profile service working");

      this.accountService.profile(this.profile).subscribe(
        (data) => {
          this.profile = data;
        }
      )
  }

  logout() {
    localStorage.clear();
  }

  openFolders() {
    this.router.navigate(["/profile/" + this.profile.id + "/folders"]);
  }
}
