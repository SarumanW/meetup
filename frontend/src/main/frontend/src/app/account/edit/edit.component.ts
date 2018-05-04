import {Component, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse, HttpEventType} from "@angular/common/http";
import {AccountService} from "../account.service";
import {Profile} from "../profile";
import {ActivatedRoute, Router} from "@angular/router"
import {UploadFileService} from "../../upload.file/upload.file.service";
import {NgxSpinnerService} from "ngx-spinner";
import {Popup} from "ng2-opd-popup";
import {FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'edit',
  styleUrls: ['./edit.component.css'],
  templateUrl: './edit.component.html'
})

export class EditComponent implements OnInit {

  success: boolean = false;
  error: string;
  selectedFiles: FileList;
  currentFileUpload: File;
  progress: { percentage: number } = {percentage: 0};
  account: Profile;
  profile: Profile;
  state: string = "profile";
  errorFileFormat: string;
  emailPattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  errorDateFormat: string;
  errorPhoneFormat: string;
  mask: any[] = ['+', '3', ' ', '8', ' ', '(', /\d/, /\d/, /\d/, ')', ' ', /\d/, /\d/, /\d/, '-', /\d/, /\d/, '-', /\d/, /\d/];
  phonePattern = /\+3\s8\s\(\d{3}\)\s\d{3}-\d{2}-\d{2}/;
  editForm = this.fb.group({
    email: [Validators.required, Validators.pattern(this.emailPattern)]
  });

  constructor(private accountService: AccountService,
              private router: Router,
              private fb: FormBuilder,
              private route: ActivatedRoute,
              private uploadService: UploadFileService,
              private spinner: NgxSpinnerService,
              private popup: Popup) {
  }

  clickButton() {
    const maxYear = `${2012}`;
    const minYear = `${1960}`;
    if (this.account.birthDay > maxYear || this.account.birthDay < minYear ) {
      this.errorDateFormat = "Please enter your real day of birth!";
    } else {
      this.popup.show();
    }
  }

  close() {
    this.popup.hide();
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
    this.editForm.get('email').setValidators(Validators.email);
    this.account = new Profile();
    this.route.params.subscribe(params => {
      this.account.login = params['login'];
    });

    this.accountService.profile(this.account.login).subscribe(
      (data) => {
        this.account = data;
      }
    );
  }

  save() {
    this.spinner.show();

    this.accountService.update(this.account).subscribe(
      () => {
        this.success = true;
        this.spinner.hide();
      },
      response => {
        if (response.status === 200) {
          let profile = JSON.parse(localStorage.currentUser);
          profile.name = this.account.name;
          profile.lastname = this.account.lastname;
          profile.email = this.account.email;
          profile.birthDay = this.account.birthDay;
          profile.phone = this.account.phone;
          //profile.imgPath = .body;
          localStorage.setItem('currentUser', JSON.stringify(profile));

          this.router.navigate(
            [JSON.parse(localStorage.currentUser).login + '/profile']);
        }
        this.spinner.hide();
        this.processError(response)
      }
    );
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
    const filename: string = this.selectedFiles.item(0).name.toLowerCase();
    if (!filename.endsWith(".jpg") &&
      !filename.endsWith(".png") &&
      !filename.endsWith(".gif")) {
      this.errorFileFormat = "Bad format for file " + this.selectedFiles.item(0).name;
    } else {
      this.errorFileFormat = null;
    }
  }

  upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        console.log('File is completely uploaded!');
        console.log(event.body);
        let profile = JSON.parse(localStorage.currentUser);
        profile.imgPath = event.body;
        localStorage.setItem('currentUser', JSON.stringify(profile));
      }
    });

    this.selectedFiles = undefined

  }

  checkPhone(phone: String){
    if (!phone.match(this.phonePattern)){
      this.errorPhoneFormat = "error";
    }
    else {
      this.errorPhoneFormat = '';
    }
  }

  formatDate(date: Date)  {
    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    this.account.birthDay = `${day}-${month}-${year}`;
  }


  private processError(response: HttpErrorResponse) {
    this.success = null;
    console.log(response);
    this.error = 'ERROR';
  }
}
