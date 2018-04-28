import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpResponse, HttpEventType} from "@angular/common/http";
import {AccountService} from "../account.service";
import {Profile} from "../profile";
import {ActivatedRoute, Router} from "@angular/router"
import {UploadFileService} from "../../upload.file/upload.file.service";
import {NgxSpinnerService} from "ngx-spinner";

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


  constructor(private accountService: AccountService,
              private router: Router,
              private route: ActivatedRoute,
              private uploadService: UploadFileService,
              private spinner: NgxSpinnerService) {

    this.account = new Profile();
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
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
          if (JSON.parse(localStorage.currentUser).login != this.account.login) {
            let profile = JSON.parse(localStorage.currentUser);
            profile.login = this.account.login;
            localStorage.setItem('currentUser', JSON.stringify(profile));
          }

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
    var filename: string = this.selectedFiles.item(0).name.toLowerCase();
    if (!filename.endsWith(".jpg") &&
      !filename.endsWith(".png") &&
      !filename.endsWith(".gif")) {
      this.errorFileFormat = "Bad fromat for file " + this.selectedFiles.item(0).name;
    } else {
      this.errorFileFormat = null;
    }
  }

  upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0)
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        console.log('File is completely uploaded!');
      }
    })

    this.selectedFiles = undefined
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
