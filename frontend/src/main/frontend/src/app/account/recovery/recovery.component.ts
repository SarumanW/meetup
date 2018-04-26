import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {RecoveryProfile} from "../recovery.profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router"

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
              private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.success = false;
    this.recovery = new RecoveryProfile();
    this.route.params.subscribe(params => {
      this.recovery.token = params['token'];
    });
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
        },
        response => this.processError(response)
      );
    }
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    console.log(response);
    this.error = response.error;
  }
}
