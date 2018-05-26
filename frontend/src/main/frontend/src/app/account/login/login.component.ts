import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {AccountService} from "../account.service";
import {LoginAccount} from "../login.account";
import {Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  success: boolean;
  account: LoginAccount;
  errorMessage: string;

  constructor(private accountService: AccountService,
              private router: Router,
              private spinner: NgxSpinnerService,
              private appComponent: AppComponent) {
  }

  ngOnInit() {
    this.success = false;
    this.account = new LoginAccount();
  }

  logIn() {
    this.spinner.show();

    this.accountService.login(this.account).subscribe(
      (profile) => {
        this.success = true;
        this.spinner.hide();
        this.router.navigate(
          ['/' + profile.login + '/profile']);
      }, error => {
        this.appComponent.showError(error, 'Error');
        this.processError(error);
        this.spinner.hide();
      }
    );
  }


  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.status !== 401) {
      this.errorMessage = response.error;
    }
  }
}
