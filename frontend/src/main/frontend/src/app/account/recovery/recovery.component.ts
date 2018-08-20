import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {RecoveryProfile} from "../recovery.profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router"
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-recovery',
  templateUrl: './recovery.component.html'
})
export class RecoveryComponent implements OnInit {
  confirmPassword: string;
  doNotMatch: string;
  error: string;
  success: boolean;
  recovery: RecoveryProfile;

  constructor(private accountService: AccountService,
              private router: Router, private route: ActivatedRoute,
              private appComponent: AppComponent) {
  }

  ngOnInit() {

    this.success = false;
    this.recovery = new RecoveryProfile();

    this.route.params.subscribe(params => {
      this.recovery.token = params['token'];
    },error => {
        this.appComponent.showError(error, 'Upload failed');
      }
    );

  }

  recover() {
    if (this.recovery.password !== this.confirmPassword) {
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;
      this.accountService.recovery(this.recovery).subscribe(
        () => {
          this.success = true;
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 10000);
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
