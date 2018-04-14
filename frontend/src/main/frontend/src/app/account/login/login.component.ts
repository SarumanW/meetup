import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {Account} from "../account";
import {AccountService} from "../account.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  success: boolean;
  account: Account;

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.success = false;
    this.account = new Account();
  }

  logIn() {
      this.accountService.save(this.account).subscribe(
        () => {
          this.success = true;
        },
        response => this.processError(response)
      );
    }


  private processError(response: HttpErrorResponse) {
    this.success = null;
  }
}
