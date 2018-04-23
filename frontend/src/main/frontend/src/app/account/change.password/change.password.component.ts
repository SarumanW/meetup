import {Component, OnInit} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router"
import {RecoveryProfile} from "../recovery.profile";

@Component({
  selector: 'change.password',
  templateUrl: './change.password.component.html'
})

export class ChangePasswordComponent implements OnInit {
  success: boolean;
  confirmPassword: string;
  password: string;
  error: string;
  account: RecoveryProfile;
  doNotMatch: string;
  lastName: string;

  constructor(private accountService: AccountService,
              private router: Router, private route: ActivatedRoute) {
  }

    ngOnInit(): void {
    this.success = false;
    this.account = new RecoveryProfile();
    this.route.params.subscribe(params => {
      this.account.token = params['token'];
    });
  }

  passwordConfirm() {
    if (this.account.newPassword !== this.confirmPassword) {
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;
      this.accountService.save(this.account).subscribe(
        () => {
          this.success = true;
        },
        response => this.processError(response)
      );
    }
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    console.log(response);
    this.error = 'ERROR';
  }
}
