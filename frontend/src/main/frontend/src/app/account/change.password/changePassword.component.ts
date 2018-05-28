import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {RecoveryProfile} from "../recovery.profile";
import {ActivatedRoute, Router} from "@angular/router"
import {NgxSpinnerService} from "ngx-spinner";
import {AccountService} from "../account.service";
import {ToastrService} from "ngx-toastr";
import {Profile} from "../profile";
import {AppComponent} from "../../app.component";


@Component({
  selector: 'change.password',
  templateUrl: './changePassword.component.html'
})

export class ChangePasswordComponent implements OnInit {

  confirmNewPassword: string;
  doNotMatch: string;
  error: string;
  success: boolean;
  account: Profile;
  recovery: RecoveryProfile;
  loggedUser: boolean;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private spinner: NgxSpinnerService,
              private toastr: ToastrService,
              private accountService: AccountService,
              private appComponent:AppComponent) {
  }

  ngOnInit() {
    this.account = JSON.parse(localStorage.getItem('currentUser'));
    this.account = new Profile();
    this.recovery = new RecoveryProfile();
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
      });
  }

  changePassword() {
    this.spinner.show();
     if (this.recovery.password !== this.confirmNewPassword) {
      this.doNotMatch = 'ERROR';
    }
    else {
      this.doNotMatch = null;
           this.accountService.changePassword(this.recovery).subscribe(
        () => {
          this.success = true;
          this.router.navigate(
            [JSON.parse(localStorage.currentUser).login + '/profile']);
          this.showSuccess('Your password successfully edited', 'Success!');
          this.spinner.hide();
        } ,error => {
               this.appComponent.showError(error, 'Upload failed');
               this.processError(error)
             }
      );
    }
  }

  showSuccess(message: string, title: string) {
    this.toastr.info(message, title, {
      timeOut: 3000,
      positionClass: 'toast-top-right',
      closeButton: true
    });
  }

  private processError(response: HttpErrorResponse) {
      this.success = null;
      this.error = response.error;
    }
}
