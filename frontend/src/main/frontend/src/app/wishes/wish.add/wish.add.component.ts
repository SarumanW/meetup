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

  upload() {
    if (this.selectedFile !== null) {
      this.spinner.show();
      this.uploadService.pushWishFileToStorage(this.selectedFile).subscribe(event => {
        if (event instanceof HttpResponse) {
          console.log('File is completely uploaded!');
          console.log(event.body);
          //todo Check working
          //let profile = JSON.parse(localStorage.currentUser);
          //profile.imgPath = event.body;
          //this.newItem.imageFilepath = event.body.valueOf();
          //localStorage.setItem('currentUser', JSON.stringify(profile));
        }
      });
      this.spinner.hide();
      this.selectedFile = null;
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

  showSuccess() {
    this.toastr.info('Wish item was successfully added', 'Attention!', {
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

  addWish() {
    this.newItem.ownerId = this.profile.id;
    this.spinner.show();
    this.wishListService.addWishItem(this.newItem).subscribe(item => {
      this.spinner.hide();
      this.showSuccess();
      this.resetItem();
    }, error => {
      this.showError('Unsuccessful wish item adding', 'Adding error');
      this.spinner.hide();
    });
  }

}
