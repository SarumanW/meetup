import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {HttpClient} from '@angular/common/http';
import {FormBuilder, Validators} from "@angular/forms";

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
    email: ['', [Validators.required, Validators.pattern(this.emailPattern)]]
  });

  constructor(private http: HttpClient,
              private fb: FormBuilder) {
  }

  get email() {
    return this.recoveryForm.get('email');
  }

  ngOnInit() {
    this.success = false;
  }

  sendRecovery() {
    console.log(this.emailAddr);
    this.http.get('api/recovery/'+this.emailAddr).subscribe(
      () => {
        this.success = true;
      },
      response => this.processError(response)
    );
  }

  processError(response: HttpErrorResponse) {
    this.success = null;
    console.log(response);
    this.error = response.error;
  }
}
