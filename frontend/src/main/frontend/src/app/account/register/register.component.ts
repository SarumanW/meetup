import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {RegisterAccount} from "../register.account";
import {AccountService} from "../account.service";
import {Router} from "@angular/router"
import {FormBuilder, Validators} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})

export class RegisterComponent implements OnInit {
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorLoginExists: string;
  success: boolean;
  account: RegisterAccount;
  emailPattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  registerForm = this.fb.group({
    email: [Validators.required, Validators.pattern(this.emailPattern)]
  });

  constructor(private accountService: AccountService,
              private fb: FormBuilder,
              private router: Router,
              private spinner: NgxSpinnerService,
              private appComponent: AppComponent) {
  }

  ngOnInit() {
    this.success = false;
    this.account = new RegisterAccount();
    this.registerForm.get('email').setValidators(Validators.email);
  }

  register() {
    this.spinner.show();

    this.doNotMatch = null;

    this.accountService.save(this.account).subscribe(
      () => {
        this.success = true;
        this.error = null;
        this.errorEmailExists = null;
        this.errorLoginExists = null;
        this.spinner.hide();
      }, error => {
        this.appComponent.showError(error, 'Error');
        this.processError(error);
        this.spinner.hide();
      }
    );

  }

  get email() {
    return this.registerForm.get('email');
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.error === 'Login already used') {
      this.success = null
      this.error = null;
      this.errorEmailExists = null;
      this.errorLoginExists = 'ERROR';
    } else if (response.error === 'Email already used') {
      this.success = null
      this.error = null;
      this.errorLoginExists = null;
      this.errorEmailExists = 'ERROR';
    } else {
      this.success = null
      this.errorEmailExists = null;
      this.errorLoginExists = null;
      this.error = 'ERROR';
    }

  }
}
