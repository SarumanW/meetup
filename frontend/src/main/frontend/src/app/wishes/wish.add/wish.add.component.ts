import {Component, OnInit} from '@angular/core';
import {Item} from "../item";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {UploadFileService} from "../../upload.file/upload.file.service";
import {HttpClientModule} from "@angular/common/http";
import {Tag} from "../tag";
import {ITEMS} from "../items";
import {NgxSpinnerService} from "ngx-spinner";
import {WishListService} from "../wish.list.service";
import {ToastrService} from "ngx-toastr";
import {Profile} from "../../account/profile";
import {Observable} from "rxjs/Observable";
import {Subscriber} from "rxjs/Subscriber";

@Component({
  selector: 'app-wish-add',
  templateUrl: './wish.add.component.html',
  styleUrls: ['./wish.add.component.css']
})
export class WishAddComponent implements OnInit {
  state: string = "wishes";
  newItem: Item;

  // Image
  selectedFile = null;

  //Date
  minDueDate: string;

  //Tag
  tag: string;

  //Profile
  profile: Profile;

  constructor(private uploadService: UploadFileService,
              private toastr: ToastrService,
              private spinner: NgxSpinnerService,
              private wishListService: WishListService) {
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
    this.newItem = new Item();
    this.getDueDate();
  }

  getDueDate() {
    let today = new Date();
    let year = today.getFullYear();
    let month = today.getMonth() + 1;
    let day = today.getDate();

    this.minDueDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    this.newItem.dueDate = this.minDueDate;

    console.log(this.minDueDate);
    console.log(this.newItem.dueDate);
  }

  selectFile(event) {
    if (event.target.files && event.target.files[0]) {
      let reader = new FileReader();

      reader.onload = (event:any) => {
        this.selectedFile = event.target.result;
      };

      reader.readAsDataURL(event.target.files[0]);
    }
  }

  addTag() {
    if (this.tag.length > 2 && this.tag.length < 31 && /^[_A-Za-z0-9]*$/.test(this.tag)) {
      // let tag = new Tag();
      // tag.name = this.tag;

      this.newItem.tags.push(this.tag);

      this.tag = '';
    }
  }

  deleteTag(tag: string) {
    const index = this.newItem.tags.indexOf(this.tag);
    console.log(index);
    if (index !== -1) {
      this.newItem.tags.splice(index, 1)
    }
  }

  resetItem() {
    this.newItem = new Item();
    this.getDueDate();
    this.selectedFile = null;
    this.tag = '';
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
    let dateAndTime = this.newItem.dueDate;
    dateAndTime += ' 00:00:00';
    this.newItem.dueDate = dateAndTime;
  }

  onSubmit() {
    this.newItem.ownerId = this.profile.id;

    this.setCorrectDate();

    this.spinner.show();

    if(this.selectedFile !== null) {
      this.uploadImage();
    } else {
      this.addWish();
    }
  }

  uploadImage() {
    this.uploadService.pushWishFileToStorage(this.selectedFile).subscribe(event => {
      if (event instanceof HttpResponse) {
        this.showError('Unsuccessful image uploaded', 'Adding error');
        console.log('File is completely uploaded!');
        console.log(event.body);

        //todo Check working
        this.newItem.imageFilepath = event.body;
        this.addWish();
        this.selectedFile = null;
      }
    }, error => {
      this.showError('Unsuccessful image uploaded', 'Adding error');
    });
  }

  addWish() {
    console.log('run "add wish" method');
    this.wishListService.addWishItem(this.newItem).subscribe(item => {
      this.spinner.hide();
      this.showSuccess('Wish item was successfully added', 'Attention!');
      this.resetItem();
    }, error => {
      this.showError('Unsuccessful wish item adding', 'Adding error');
      this.spinner.hide();
    });
  }
}
