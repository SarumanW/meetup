import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {AccountService} from "../account.service";
import {LoginAccount} from "../login.account";
import {Router, ActivatedRoute} from "@angular/router";
import {MessageService} from "../message.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [ './login.component.css' ]
})
export class LoginComponent implements OnInit {
  success: boolean;
  account: LoginAccount;
  errorMessage: string;

  constructor(private accountService: AccountService,
              private router: Router,
              public messageService : MessageService) {
  }

  ngOnInit() {
    this.success = false;
    this.account = new LoginAccount();
  }

  logIn() {
      this.accountService.login(this.account).subscribe(
        (user) => {
            console.log("login component working")
            this.success = true;
            this.account = user;
            this.messageService.clear();
            console.log("login data taken" + this.account.login);
            this.router.navigate(['/profile/details', this.account.login]);
        },
        response => {
          this.processError(response)
          this.router.navigate(['/login']);}
      );
    }


  private processError(response: HttpErrorResponse) {
    this.success = null;
    this.errorMessage = response.error;
  }
}
