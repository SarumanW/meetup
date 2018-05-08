import { Component, OnInit } from '@angular/core';
import {Item} from "../item";
import {WishListService} from "../wish.list.service";
import {Profile} from "../../account/profile";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";
import {Router, ActivatedRoute, Params} from "@angular/router";
import {ITEMS} from "../items";
import {Tag} from "../tag";

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

  constructor(private wishListService: WishListService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private toastr: ToastrService,) {
  }

  ngOnInit() {

    this.profile = JSON.parse(localStorage.getItem('currentUser'));
    this.category = WishListComponent.OWN_CATEGORY;
    this.title = "Own wishes:";

    this.route.params.subscribe((params: Params) => {
      this.login = params['login'];
      if(this.login === undefined) {
        this.login = this.profile.login;
      }
      this.getWishList();
    });

    // subscribe to router event
    this.route.queryParams.subscribe((params: Params) => {

      switch (params['category']) {
        case WishListComponent.BOOKINGS_CATEGORY: {
          this.category = WishListComponent.BOOKINGS_CATEGORY;
          this.title = "Bookings wishes:";
          break;
        }
        case WishListComponent.RECOMMENDATIONS_CATEGORY: {
          if (this.login === this.profile.login) {
            this.category = WishListComponent.RECOMMENDATIONS_CATEGORY;
            this.title = "Recommendation wishes:";
            break;
          }
        }
        default: {
          this.category = WishListComponent.OWN_CATEGORY;
          this.title = "Own wishes:";
          break;
        }
      }

      this.getWishList();
    });

    console.log(this.items);

    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }

  getWishList(withSpinner = true) {
    //todo get wish list from database
    if (withSpinner) {
      this.spinner.show();
    }

    this.wishListService.getWishList(this.category, this.login).subscribe(
      itemList => {
        this.items = itemList;
        this.spinner.hide();
      });
  }

  addSearchTag() {
    if (this.tag.length > 2 && this.tag.length < 31 && /^[_A-Za-z0-9]*$/.test(this.tag)) {
      this.tags.push(this.tag);
      this.tag = '';
      //todo search items by tag from database
    }
  }

  deleteSearchTag(tag: string) {
    const index = this.tags.indexOf(tag);
    console.log(index);
    if (index !== -1) {
      this.tags.splice(index, 1)
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

  addWishItem(item: Item) {
    item.ownerId = this.profile.id;
    this.spinner.show();
    this.wishListService.addWishItem(item).subscribe(item => {
      this.spinner.hide();
      this.items.splice(item);
      //this.getWishList(false);
      this.showSuccess('Wish item was successfully added', 'Attention!');
    }, error => {
      this.showError('Unsuccessful wish item adding', 'Adding error');
      this.spinner.hide();
    });
  }

  deleteWishItem(item: Item) {
    this.spinner.show();
    this.wishListService.deleteWishItem(item).subscribe(item => {
      this.spinner.hide();
      this.items.splice(item);
      //this.getWishList(false);
      this.showSuccess('Wish item was successfully deleted', 'Attention!');
    }, error => {
      this.showError('Unsuccessful wish item deleting', 'Adding error');
      this.spinner.hide();
    });
  }

  bookWishItem(item: Item) {
    this.spinner.show();
    this.wishListService.deleteWishItem(item).subscribe(item => {
      this.spinner.hide();
      this.items.splice(item);
      //this.getWishList(false);
      this.showSuccess('Wish item was successfully deleted', 'Attention!');
    }, error => {
      this.showError('Unsuccessful wish item deleting', 'Adding error');
      this.spinner.hide();
    });
  }

  unbookWishItem(item: Item) {
    this.spinner.show();
    this.wishListService.deleteWishItem(item).subscribe(item => {
      this.spinner.hide();
      this.items.splice(item);
      //this.getWishList(false);
      this.showSuccess('Wish item was successfully deleted', 'Attention!');
    }, error => {
      this.showError('Unsuccessful wish item deleting', 'Adding error');
      this.spinner.hide();
    });
  }
}
