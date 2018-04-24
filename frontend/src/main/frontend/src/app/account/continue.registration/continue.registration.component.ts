import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router"

@Component({
  selector: 'continueRegistration',
  templateUrl: './continue.registration.component.html'
})


export class ContinueRegistrationComponent implements OnInit {

  success: boolean;
  imgPath = null;
  // birthDay: string;
  // phone: string
  error: string;
  account: Profile;


  constructor(private accountService: AccountService,
              private router: Router, private route: ActivatedRoute, private  http: HttpClient) {
  }


  ngOnInit() {
    this.success = false;
    this.account = new Profile();
    this.route.params.subscribe(params => {
      this.account.login = params['login'];
    });
  }

  onFileSelected(event) {
    this.imgPath = <File>event.target.files[0];
  }

  save(){

    this.accountService.update(this.account).subscribe(
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
    this.account.birthDay = `${day}-${month}-${year}`;
    // this.birthDay = `${day}-${month}-${year}`;
    // this.accountService.save(this.account).subscribe(
    //   () => {
    //     this.success = true;
    //   },
    //   response => this.processError(response)
    // );
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    this.error = 'ERROR';
  }

  changeWishList() {

  }
}
