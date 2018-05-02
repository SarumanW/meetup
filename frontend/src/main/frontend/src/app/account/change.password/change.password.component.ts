import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {RecoveryProfile} from "../recovery.profile";
import {ActivatedRoute} from "@angular/router"
import {NgxSpinnerService} from "ngx-spinner";
import {LoginAccount} from "../login.account";
import {AccountService} from "../account.service";

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
  profile: RecoveryProfile;
  output: any;
  account: LoginAccount;
  newPassword: string;
  oldPassword: string;

  constructor(private route: ActivatedRoute,
              private spinner: NgxSpinnerService,
              private accountService: AccountService) {
  }

  ngOnInit() {
    this.success = false;
    this.profile = new RecoveryProfile();
    this.route.params.subscribe(params => {
      this.profile.token = params['token'];
    });
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
      this.accountService.update(this.account).subscribe(
        () => {
          this.success = true;
          this.spinner.hide();
        },
         response => this.processError(response));
     this.output = this.error;
     this.spinner.hide();
    }
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    console.log(response);
    this.error = response.error;
  }
}
