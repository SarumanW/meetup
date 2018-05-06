import { Component, OnInit } from '@angular/core';
import {Item} from "../item";
import {WishListService} from "../wish.list.service";
import {Profile} from "../../account/profile";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";
import {Router, ActivatedRoute, Params} from "@angular/router";

@Component({
  selector: 'app-wish-list',
  templateUrl: './wish.list.component.html',
  styleUrls: ['./wish.list.component.css']
})
export class WishListComponent implements OnInit {
  readonly WISHES_CATEGORY = "wishes";
  readonly RECOMMENDATIONS_CATEGORY = "recommendations";
  readonly POPULAR_CATEGORY = "popular";

  items: Item[];
  state: string = "wishes";
  title: string;
  category: string;
  profile: Profile;

  constructor(private wishListService: WishListService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private toastr: ToastrService,) {
  }

  ngOnInit() {

    this.profile = JSON.parse(localStorage.getItem('currentUser'));

    // subscribe to router event
    this.route.queryParams.subscribe((params: Params) => {

      switch (params['category']) {
        case this.RECOMMENDATIONS_CATEGORY: {
          this.category = this.RECOMMENDATIONS_CATEGORY;
          this.title = "Recommendation wishes:";
          break;
        }
        case this.POPULAR_CATEGORY: {
          this.category = this.POPULAR_CATEGORY;
          this.title = "Popular wishes:";
          break;
        }
        default: {
          this.category = this.WISHES_CATEGORY;
          this.title = "My wishes:";
          break;
        }
      }

      this.getWishList();
    });

    this.spinner.show();

    this.getWishList();

    this.spinner.hide();

    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }

  getWishList() {
    this.spinner.show();

    this.wishListService.getWishList(this.category).subscribe(
      itemList => {
        this.items = itemList;
        this.spinner.hide();
      })
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

  addWishItem(item: Item) {
    item.ownerId = this.profile.id;
    this.spinner.show();
    this.wishListService.addWishItem(item).subscribe(item => {
      this.spinner.hide();
      this.getWishList();
      this.showSuccess();
    }, error => {
      this.showError('Unsuccessful wish item adding', 'Adding error');
      this.spinner.hide();
    });
  }

  deleteWishItem(item: Item) {
    this.spinner.show();
    this.wishListService.deleteWishItem(item).subscribe(item => {
      this.spinner.hide();
      this.getWishList();
      this.showSuccess();
    }, error => {
      this.showError('Unsuccessful wish item adding', 'Adding error');
      this.spinner.hide();
    });
  }
}
