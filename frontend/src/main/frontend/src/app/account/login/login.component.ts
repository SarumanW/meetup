import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {AccountService} from "../account.service";
import {LoginAccount} from "../login.account";
import {Router, ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [ './login.component.css' ]
})
export class LoginComponent implements OnInit {
  success: boolean;
  account: LoginAccount;
  returnUrl: string;

  constructor(private accountService: AccountService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.success = false;
    this.account = new LoginAccount();

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/success';
  }

  logIn() {
      this.accountService.login(this.account).subscribe(
        (data) => {
          console.log("login component working")
          this.success = true;
          console.log(data);
          this.router.navigate([this.returnUrl])
        },
        response => this.processError(response)
      );
    }


  private processError(response: HttpErrorResponse) {
    this.success = null;
  }
}
