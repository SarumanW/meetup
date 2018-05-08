import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router"
import {NgxSpinnerService} from "ngx-spinner";
import {AccountService} from "../../account.service";
import {Profile} from "../../profile";

@Component({
  selector: 'check.password',
  templateUrl: './check.password.component.html'
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
              private router: Router) {
  }

  ngOnInit() {
    this.account = JSON.parse(localStorage.getItem('currentUser'));
    this.account = new Profile();
    this.route.params.subscribe(params => {
      this.account.login = params['login'];
    });

    this.accountService.profile(this.account.login).subscribe(
      (data) => {
        this.account = data;
        this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === this.account.login;
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
      },
      response => this.processError(response)
    );
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    console.log(response);
    this.error = response.error;
  }
}
