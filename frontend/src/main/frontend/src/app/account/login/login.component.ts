import {Component, OnInit, ViewChild} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {AccountService} from "../account.service";
import {LoginAccount} from "../login.account";
import {Router, ActivatedRoute} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [ './login.component.css' ]
})
export class LoginComponent implements OnInit {
  success: boolean;
  account: LoginAccount;
  errorMessage: string;

  @ViewChild(AppComponent) childComponent: AppComponent
  constructor(private accountService: AccountService,
              private router: Router,
              private spinner: NgxSpinnerService) {
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
              ['/'+ profile.login + '/profile']);
        },
        response => {
          if (response.status === 418) {
            this.childComponent.showError('The server encountered an error but still retry your request. Please wait..', 'Server error!');
          } else {
            this.processError(response);
            this.spinner.hide();
          }
        }
      );
    }


  private processError(response: HttpErrorResponse) {
    this.success = null;
    this.errorMessage = response.error;
  }
}
