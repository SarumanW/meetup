import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-sendrecovery',
  templateUrl: './send.recovery.component.html'
})
export class SendRecoveryComponent implements OnInit {
  emailAddr: string;
  error: string;
  success: boolean;

  constructor(private http: HttpClient) {
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

  private

  processError(response: HttpErrorResponse) {
    this.success = null;
    console.log(response);
    this.error = response.error;
  }
}
