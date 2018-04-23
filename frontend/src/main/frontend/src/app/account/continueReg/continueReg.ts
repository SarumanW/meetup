import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router"

@Component({
  selector: 'continueReg',
  templateUrl: './continueReg.html'
})


export class ContinueRegComponent implements OnInit {

  success: boolean;
  login: string;
  email: string;
  name: string;
  imgPath = null;
  birthDay: string;
  error: string;
  errorEmailExists: string;
  errorLoginExists: string;
  account: Profile;


  constructor(private accountService: AccountService,
              private router: Router, private route: ActivatedRoute, private  http: HttpClient) {
  }


  ngOnInit() {
    this.success = false;
    this.account = new Profile();
    this.route.params.subscribe(params => {
      this.account.token = params['token'];
    });
  }


  onFileSelected(event) {
    this.imgPath = <File>event.target.files[0];
    this.accountService.save(this.account).subscribe(
      () => {
        this.success = true;
      },
      response => this.processError(response)
    );
  }

  onUpload() {
    const fd = new FormData();
    fd.append('image', this.imgPath, this.imgPath.name);
    this.accountService.save(this.account).subscribe(
      () => {
        this.success = true;
      },
      response => this.processError(response)
    );
  }

  formatDate(date: Date) {
    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    this.birthDay = `${day}-${month}-${year}`;
    this.accountService.save(this.account).subscribe(
      () => {
        this.success = true;
      },
      response => this.processError(response)
    );
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.error === 'Login already used') {
      this.errorLoginExists = 'ERROR';
    } else if (response.error === 'Email already used') {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }

  }

  checkSomething() {

  }

  changeWishList() {

  }
}
