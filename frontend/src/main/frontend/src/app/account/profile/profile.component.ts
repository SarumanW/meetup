import {Component} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent {

  state: string = "profile";
  profile: Profile;
  loggedUser: boolean;

  constructor(private accountService: AccountService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.spinner.show();

    this.route.params.subscribe(params => {
      this.accountService.profile(params['login']).subscribe(
        (profile) => {
          this.spinner.hide();
          this.profile = profile;

          this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).id === this.profile.id;
        })})
  }
}
