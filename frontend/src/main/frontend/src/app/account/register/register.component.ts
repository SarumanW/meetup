import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {AccountRegister} from "../account";
import {AccountService} from "../account.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {
  confirmPassword: string;
  doNotMatch: string;
  success: boolean;
  registerAccount: AccountRegister;

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.success = false;
    this.registerAccount = new AccountRegister();
  }

  register() {
    if (this.registerAccount.password !== this.confirmPassword) {
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;
      this.accountService.save(this.registerAccount).subscribe(
        () => {
          this.success = true;
        },
        response => this.processError(response)
      );
    }
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
  }
}
