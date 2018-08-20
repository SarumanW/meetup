import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {HttpClient} from '@angular/common/http';
import {FormBuilder, Validators} from "@angular/forms";
import {AppComponent} from "../../app.component";
import {AccountService} from "../account.service";

@Component({
  selector: 'app-sendrecovery',
  templateUrl: './send.recovery.component.html'
})
export class SendRecoveryComponent implements OnInit {
  emailAddr: string;
  error: string;
  success: boolean;
  emailPattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  recoveryForm = this.fb.group({
    email: [Validators.required, Validators.pattern(this.emailPattern)]
  });

  constructor(private http: HttpClient,
              private fb: FormBuilder,
              private appComponent: AppComponent,
              private accountService: AccountService,) {
  }

  ngOnInit() {
    this.success = false;
    this.recoveryForm.get('email').setValidators(Validators.email);
  }

  sendRecovery() {
    this.accountService.sendRecovery(this.emailAddr).subscribe(
      () => {
        this.success = true;
      }, error => {
        this.appComponent.showError(error, 'Error');
        this.processError(error)
      }
    );
  }

  processError(response: HttpErrorResponse) {
    this.success = null;
    this.error = response.error;
  }
}
