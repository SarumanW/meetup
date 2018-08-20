import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {RecoveryProfile} from "../recovery.profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router"
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-confirm',
  templateUrl: './confirmation.component.html'
})
export class ConfirmationComponent implements OnInit {
  confirmPassword: string;
  doNotMatch: string;
  error: string;
  success: boolean;
  confirmation: RecoveryProfile;

  constructor(private accountService: AccountService,
              private router: Router, private route: ActivatedRoute,
              private appComponent: AppComponent) {
  }

  ngOnInit() {
    this.success = false;
    this.confirmation = new RecoveryProfile();

    this.route.params.subscribe(params => {
        this.confirmation.token = params['token'];
      },error => {
        this.appComponent.showError(error, 'Upload failed');
      }
    );
  }

  confirm() {
    if (this.confirmation.password !== this.confirmPassword) {
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;
      this.accountService.confirm(this.confirmation).subscribe(
        () => {
          this.success = true;
          this.router.navigate(['/thankyou']);
        },error => {
          this.appComponent.showError(error, 'Upload failed');
          this.processError(error)
        }
      );
    }
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    this.error = response.error;
  }
}
