import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router"
import {NgxSpinnerService} from "ngx-spinner";
import {AccountService} from "../../account.service";
import {Profile} from "../../profile";
import {AppComponent} from "../../../app.component";

@Component({
  selector: 'check.password',
  templateUrl: './checkPassword.component.html'
})

export class CheckPasswordComponent implements OnInit {

  wrongPassword: string;
  error: string;
  success: boolean;
  account: Profile;
  loggedUser: boolean;

  constructor(private route: ActivatedRoute,
              private spinner: NgxSpinnerService,
              private accountService: AccountService,
              private router: Router,
              private appComponent: AppComponent) {
  }

  ngOnInit() {
    this.account = JSON.parse(localStorage.getItem('currentUser'));
    this.account = new Profile();
    this.route.params.subscribe(params => {
      this.account.login = params['login'];
    },error => {
      this.appComponent.showError(error, 'Upload failed');
    });

    this.accountService.profile(this.account.login).subscribe(
      (data) => {
        this.account = data;
        this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === this.account.login;
      },error => {
        this.appComponent.showError(error, 'Upload failed');
      }
    );
  }

  checkPassword() {
    this.spinner.show();
    this.accountService.checkPassword(this.account).subscribe(
      () => {
        this.success = true;
        setTimeout(() => {
          this.router.navigate(["/" + this.account.login + "/change.password"]);
        }, 10);
        this.spinner.hide();
      }, error => {
        this.appComponent.showError(error, 'Upload failed');
        this.processError(error)
      }
    );
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    this.error = response.error;
  }
}
