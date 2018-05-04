import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {RecoveryProfile} from "../recovery.profile";
import {ActivatedRoute, Router} from "@angular/router"
import {NgxSpinnerService} from "ngx-spinner";
import {AccountService} from "../account.service";
import {Profile} from "../profile";

@Component({
  selector: 'change.password',
  templateUrl: './change.password.component.html'
})

export class ChangePasswordComponent implements OnInit {
  confirmNewPassword: string;
  doNotMatch: string;
  wrongPassword: string;
  error: string;
  success: boolean;
  recovery: RecoveryProfile;
  account: Profile;
  profile: Profile;
  loggedUser: boolean;
  newPassword: string;
  oldPassword: string;

  constructor(private route: ActivatedRoute,
              private spinner: NgxSpinnerService,
              private router: Router,
              private accountService: AccountService) {
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
    this.account = new Profile();
    this.route.params.subscribe(params => {
      this.account.login = params['login'];
    });

    this.accountService.profile(this.account.login).subscribe(
      (data) => {
        this.account = data;
        this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === this.profile.login;
      }
    );
  }

  changePassword() {
    this.spinner.show();
    if (this.newPassword !== this.confirmNewPassword) {
      this.doNotMatch = 'ERROR';
    }
    // else if (this.oldPassword !== this.account.password) {
    //   this.wrongPassword = 'ERROR';
    // }
    else {
      this.doNotMatch = null;
      this.accountService.changePassword(this.account).subscribe(
        () => {
          this.success = true;
          this.spinner.hide();
        },
        response => this.processError(response)
      );
    }
  }

  private processError(response: HttpErrorResponse) {
      this.success = null;
      console.log(response);
      this.error = response.error;
    }
}
