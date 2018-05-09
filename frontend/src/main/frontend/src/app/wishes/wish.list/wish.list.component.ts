import { Component, OnInit } from '@angular/core';
import {Item} from "../item";
import {WishListService} from "../wish.list.service";
import {Profile} from "../../account/profile";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";
import {Router, ActivatedRoute, Params} from "@angular/router";
import "rxjs/add/operator/debounceTime";
import {WishService} from "../wish.service";

@Component({
  selector: 'app-wish-list',
  templateUrl: './wish.list.component.html',
  styleUrls: ['./wish.list.component.css']
})
export class WishListComponent implements OnInit {
  public static readonly OWN_CATEGORY = "own";
  public static readonly RECOMMENDATIONS_CATEGORY = "recommendations";
  public static readonly BOOKINGS_CATEGORY = "bookings";

  public class = WishListComponent;

  items: Item[];
  state: string = "wishes";
  title: string;
  category: string;
  profile: Profile;
  tag: string;
  tags: string[] = [];
  login: string;

  //Add item date
  minDueDate: string;
  dueDate: string;
  priority: string;

  constructor(private wishListService: WishListService,
              private wishService: WishService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private toastr: ToastrService,) {
  }

  ngOnInit() {
    this.login = this.route.snapshot.params['login'];
    this.category = WishListComponent.OWN_CATEGORY;
    this.title = "Own wishes:";

    this.profile = JSON.parse(localStorage.getItem('currentUser'));

    this.loginSubscriber();
    this.getDueDate()
  }

  loginSubscriber() {
    this.route.params.subscribe((params: Params) => {
      this.login = params['login'];
      if(this.login === undefined) {
        this.login = this.profile.login;
      }
      let category = params['category'];
      if (this.login === this.profile.login && category == WishListComponent.BOOKINGS_CATEGORY) {
        this.category = WishListComponent.BOOKINGS_CATEGORY;
        this.title = "Bookings wishes:";
      } else if (this.login === this.profile.login && category == WishListComponent.RECOMMENDATIONS_CATEGORY) {
        this.category = WishListComponent.RECOMMENDATIONS_CATEGORY;
        this.title = "Recommendation wishes:";
      } else {
        this.category = WishListComponent.OWN_CATEGORY;
        this.title = "Own wishes:";
      }
      this.getWishList();
    });
  }

  getWishList(withSpinner = true) {
    if (withSpinner) {
      this.spinner.show();
    }

    this.wishListService.getWishList(this.category, this.login, this.tags).subscribe(
      itemList => {
        console.log(itemList);
        this.items = itemList;
        this.spinner.hide();
      });
  }

  addSearchTag() {
    if (this.tag.length > 2 && this.tag.length < 31 && /^[_A-Za-z0-9]*$/.test(this.tag) && this.tags.length < 8) {
      this.tags.push(this.tag);
      this.tag = '';
      this.getWishList()
    }
  }

  deleteSearchTag(tag: string) {
    const index = this.tags.indexOf(tag);
    if (index !== -1) {
      this.tags.splice(index, 1)
      this.getWishList()
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

  //todo check working
  bookWishItem(item: Item) {
    item.bookerId = this.profile.id;
    this.spinner.show();
    this.wishService.bookWishItem(item).subscribe(itemBooked => {

      //delete one item
      const index = this.items.indexOf(item);
      if (index !== -1) {
        this.items[index] = itemBooked;
      }

      console.log(item);

      this.spinner.hide();

      this.showSuccess('Wish item was successfully booked', 'Attention!');
    }, error => {
      this.spinner.hide();
      this.showError('Unsuccessful wish item booking', 'Adding error');
    });
  }

  //todo check working
  unbookWishItem(item: Item) {
    this.spinner.show();
    this.wishService.unbookWishItem(item).subscribe(itemUnBooked => {

      //delete one item
      const index = this.items.indexOf(item);
      if (index !== -1) {
        this.items[index] = itemUnBooked;
      }

      this.spinner.hide();

      console.log(item);

      this.showSuccess('Wish item was successfully deleted', 'Attention!');
    }, error => {
      this.showError('Unsuccessful wish item deleting', 'Adding error');
      this.spinner.hide();
    });
  }

  // Add Item

  addWishItem(item: Item) {
    let newItem = Object.assign({}, item);

    console.log(item);

    newItem.ownerId = this.profile.id;
    newItem.dueDate = this.dueDate + ' 00:00:00';
    newItem.priority = this.priority;

    this.spinner.show();
    this.wishService.addWishItem(newItem).subscribe(item => {
      this.spinner.hide();

      const index = this.items.indexOf(item);
      if (index !== -1) {
        this.items.splice(index, 1)
      }

      this.showSuccess('Wish item was successfully added', 'Attention!');
    }, error => {
      this.showError('Unsuccessful wish item adding', 'Adding error');
      this.spinner.hide();
    });
  }

  getDueDate() {
    let today = new Date();
    let year = today.getFullYear();
    let month = today.getMonth() + 1;
    let day = today.getDate();

    this.minDueDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    this.dueDate = this.minDueDate;

    console.log(this.minDueDate);
    console.log(this.dueDate);
  }

  deleteWishItem(item: Item) {
    this.spinner.show();
    this.wishService.deleteWishItem(item).subscribe(item => {
      this.spinner.hide();

      const index = this.items.indexOf(item);
      if (index !== -1) {
        this.items.splice(index, 1)
      }

      this.showSuccess('Wish item was successfully deleted', 'Attention!');
    }, error => {
      this.showError('Unsuccessful wish item deleting', 'Adding error');
      this.spinner.hide();
    });
  }
}
