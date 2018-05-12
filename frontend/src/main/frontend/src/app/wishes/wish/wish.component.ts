import {Router} from "@angular/router";
import {Component, OnInit} from '@angular/core';
import {Item} from "../item";
import {UploadFileService} from "../../upload.file/upload.file.service";
import {NgxSpinnerService} from "ngx-spinner";
import {WishListService} from "../wish.list.service";
import {ToastrService} from "ngx-toastr";
import {Profile} from "../../account/profile";
import {WishService} from "../wish.service";
import {ActivatedRoute} from '@angular/router';
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-wish',
  templateUrl: './wish.component.html',
  styleUrls: ['./wish.component.css']
})
export class WishComponent implements OnInit {
  state: string = "wishes";
  success: boolean;
  errorMessage: string;
  item: Item;
  name = "ITEM";
  profile: Profile;
  id: number;
  login: string;
  private sub: any;

  minDueDate: string;
  dueDate: string;
  priority: string;


  constructor(private router: Router,
              private spinner: NgxSpinnerService,
              private uploadService: UploadFileService,
              private toastr: ToastrService,
              private wishListService: WishListService,
              private wishService: WishService,
              private route: ActivatedRoute,
              private appComponent:AppComponent) {
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.id = +params['itemId'];
      this.login = params['login'];
    },
      error => this.appComponent.showError(error, "Error"));

    this.getItem(this.id);

    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }


  like() {
    this.wishService.addLike(this.id).subscribe((item:Item)=>{
      this.item.likes = item.likes;
      this.item.isLiked = item.isLiked;
    },
      error => this.appComponent.showError(error, "Error"))
  }

  dislike(){
    this.wishService.removeLike(this.id).subscribe((item:Item)=>{
      this.item.likes = item.likes;
      this.item.isLiked = item.isLiked;
    },
      error => this.appComponent.showError(error, "Error"))
  }

  getItem(id: number) {
    this.spinner.show();

    this.wishService.getWishItem(id, this.login).subscribe(item => {
      this.item = item;
      this.spinner.hide();
    },
      error => this.appComponent.showError(error, "Error"));
  }


  removeFromWishList() {
    this.spinner.show();
    this.wishService.deleteWishItem(this.item).subscribe(deletedItem => {
      this.spinner.hide();
      this.showSuccess('Wish item was successfully deleted', 'Attention!');
      this.router.navigate(
        ['/'+ this.profile.login + '/wishes']);
    }, error => {
      this.appComponent.showError(error, "Error")
      this.spinner.hide();
    });
  }


  addToWishList() {
    let newItem = Object.assign({}, this.item);

    console.log(this.item);

    newItem.ownerId = this.profile.id;
    newItem.dueDate = this.dueDate + ' 00:00:00';
    newItem.priority = this.priority;

    this.spinner.show();
    this.wishService.addExistWishItem(newItem).subscribe(newItem => {
      this.spinner.hide();
      this.showSuccess('Wish item was successfully added', 'Attention!');
    }, error => {
      this.appComponent.showError(error, "Error")
      this.spinner.hide();
    });
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

}
