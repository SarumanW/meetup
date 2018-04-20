import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {RegisterAccount} from "../register.account";
import {AccountService} from "../account.service";
import {Router} from "@angular/router"

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {
  confirmPassword: string;
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorLoginExists: string;
  success: boolean;
  account: RegisterAccount;

  constructor(private accountService: AccountService,
              private router: Router) {
  }

  ngOnInit() {
    this.success = false;
    this.account = new RegisterAccount();
  }

  register() {
    if (this.account.password !== this.confirmPassword) {
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;
      this.accountService.save(this.account).subscribe(
        () => {
          this.success = true;
          this.router.navigate(['/login']);
        },
        response => this.processError(response)
      );
    }
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    console.log(response);
    if (response.error === 'Login already used') {
      this.errorLoginExists = 'ERROR';
    } else if (response.error === 'Email already used') {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }

  }
}
