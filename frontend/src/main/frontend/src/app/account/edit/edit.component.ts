import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {AccountService} from "../account.service";
import {Profile} from "../profile";
import {ActivatedRoute, Router} from "@angular/router"

@Component({
  selector: 'edit',
  styleUrls: ['./edit.component.css'],
  templateUrl: './edit.component.html'
})


export class EditComponent implements OnInit {

  success: boolean;
  login: string;
  email: string;
  name: string;
  wishList: string;
  phone: string;
  lastname: string;
  imgPath = null;
  birthDay: string;
  error: string;
  errorEmailExists: string;
  errorLoginExists: string;
  account: Profile;
  state: string = "profile";

  constructor(private accountService: AccountService,
              private router: Router, private route: ActivatedRoute, private  http: HttpClient) {
  }

  ngOnInit() {
    this.success = false;
    this.account = new Profile();
    this.route.params.subscribe(params => {
      this.account.id = params['id'];
    });
    this.accountService.profile(this.account).subscribe(
      (data) => {
        this.account = data;
      }
    );
    console.log(this.account);
  }

  save(){

    this.accountService.update(this.account).subscribe(
      () => {
        this.success = true;
        this.router.navigate(
          ['/profile', JSON.parse(localStorage.currentUser).id]);
      },
      response => this.processError(response)
    );
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
    this.accountService.upImg(this.account).subscribe(
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
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
      this.error = 'ERROR';
  }

  changeWishList() {

  }
}
