import { Component, OnInit } from '@angular/core';
import {Item} from "../item";
import {Profile} from "../../account/profile";
import {ToastrService} from "ngx-toastr";
import {WishListService} from "../wish.list.service";
import {UploadFileService} from "../../upload.file/upload.file.service";
import {NgxSpinnerService} from "ngx-spinner";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {WishService} from "../wish.service";
import {HttpResponse} from "@angular/common/http";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-wish-edit',
  templateUrl: './wish.edit.component.html',
  styleUrls: ['./wish.edit.component.css']
})
export class WishEditComponent implements OnInit {
  state: string = "wishes";
  editItem: Item;

  // Image
  selectedFile = null;
  selectedImage = null;

  //Date
  minDueDate: string;

  //Tag
  tag: string;

  //Profile
  profile: Profile;

  //login from path
  login:string;

  constructor(private uploadService: UploadFileService,
              private toastr: ToastrService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private wishService: WishService,
              private appComponent: AppComponent,
              private router: Router) {
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
    this.paramsSubscriber();
  }

  paramsSubscriber() {
    this.route.params.subscribe((params: Params) => {
      let id = +params['itemId'];
      this.login = params['login'];
      this.getWishItemById(id);
    },
      error => this.appComponent.showError(error, "Error"));
  }

  getWishItemById(id: number) {
    this.spinner.show();
    this.wishService.getWishItem(id, this.login).subscribe(item => {
        this.editItem = item;
        this.selectedFile = this.editItem.imageFilepath;
        this.getDueDate();
        this.spinner.hide();
      },
      error => {
        this.appComponent.showError(error, "Error");
        this.spinner.hide();
      });
  }

  getDueDate() {
    let today = new Date();
    let year = today.getFullYear();
    let month = today.getMonth() + 1;
    let day = today.getDate();

    this.minDueDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    this.editItem.dueDate = this.editItem.dueDate.split(' ')[0];
  }

  selectFile(event) {
    if (event.target.files && event.target.files[0]) {
      let reader = new FileReader();

      reader.onload = (event:any) => {
        this.selectedFile = event.target.result;
      };

      reader.readAsDataURL(event.target.files[0]);
      this.selectedImage = event.target.files[0];
    }
  }

  addTag() {
    if (this.tag.length > 2 && this.tag.length < 31 && /^[_A-Za-z0-9]*$/.test(this.tag)) {
      this.editItem.tags.push(this.tag);

      this.tag = '';
    }
  }

  deleteTag(tag: string) {
    const index = this.editItem.tags.indexOf(tag);
    if (index !== -1) {
      this.editItem.tags.splice(index, 1)
    }
  }

  showSuccess(message: string, title: string) {
    this.toastr.info(message, title, {
      timeOut: 3000,
      positionClass: 'toast-top-right',
      closeButton: true
    });
  }

  showError(message: string, title: string) {
    this.toastr.error(message, title, {
      timeOut: 3000,
      positionClass: 'toast-top-right',
      closeButton: true
    });
  }

  setCorrectDate() {
    let dateAndTime = this.editItem.dueDate;
    dateAndTime += ' 00:00:00';
    this.editItem.dueDate = dateAndTime;
  }

  onSubmit() {
    this.editItem.ownerId = this.profile.id;

    this.setCorrectDate();

    this.spinner.show();

    if(this.selectedFile !== this.editItem.imageFilepath) {
      this.uploadImage();
    } else {
      this.editItem.imageFilepath = this.selectedFile;
      this.addWish();
    }
  }

  uploadImage() {
    this.uploadService.pushWishFileToStorage(this.selectedImage).subscribe(event => {
      if (event instanceof HttpResponse) {
        this.showSuccess('Successful image uploaded', 'Attention!');

        this.editItem.imageFilepath = event.body.toString();
        this.addWish();
        this.selectedFile = 'assets/item-icon-default.svg';
        this.selectedImage = null;
      }
    }, error => {
      this.appComponent.showError(error, "Error")
      this.spinner.hide();
    });
  }

  addWish() {
    this.wishService.editWishItem(this.editItem).subscribe(item => {
      this.showSuccess('Wish item was successfully edited', 'Attention!');
      this.spinner.hide();
      this.router.navigate(['./' + this.profile.login + '/wishes/' + item.itemId]);
    }, error => {
      this.appComponent.showError(error, "Error");
      this.spinner.hide();
    });
  }

}
