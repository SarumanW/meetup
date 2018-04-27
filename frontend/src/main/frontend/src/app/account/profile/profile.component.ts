import {Component} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
 templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent {

  state:string="profile";
  profile: Profile;


  constructor(private accountService: AccountService,
              private spinner: NgxSpinnerService) {

    this.profile = new Profile();
  }

  ngOnInit() {
    this.spinner.show();
    this.profile = JSON.parse(localStorage.getItem('currentUser'));

      this.accountService.profile(this.profile).subscribe(
        (data) => {
          this.spinner.hide();
          this.profile = data;
        }
      )
  }
}
