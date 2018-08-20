import {Component, OnInit} from '@angular/core';
import {Item} from "../item";
import {WishListService} from "../wish.list.service";
import {Profile} from "../../account/profile";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";
import {ActivatedRoute, Params, Router} from "@angular/router";
import "rxjs/add/operator/debounceTime";
import {WishService} from "../wish.service";
import {FormControl} from "@angular/forms";
import {AppComponent} from "../../app.component";
import {AccountService} from "../../account/account.service";

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
  login: string;
  hide:boolean

  //Add item date
  minDueDate: string;
  dueDate: string;
  priority: string;

  //Tags
  queryTagField: FormControl = new FormControl();
  queryTags: string[] = [];
  tag: string;
  tags: string[] = [];

  constructor(private router: Router,
              private wishListService: WishListService,
              private wishService: WishService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private toastr: ToastrService,
              private appComponent: AppComponent,
              private accountService: AccountService) {
  }

  ngOnInit() {
    this.spinner.show();
    this.login = this.route.snapshot.params['login'];
    this.category = WishListComponent.OWN_CATEGORY;
    this.title = "Own wishes:";

    this.profile = JSON.parse(localStorage.getItem('currentUser'));

    this.loginSubscriber();
    this.tagsInputSubscriber();
    this.getDueDate()
  }

  loginSubscriber() {
    this.route.params.subscribe((params: Params) => {
        this.login = params['login'];
        if (this.login === undefined) {
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
          this.title = " Your wishes:";
        }

        let tag = params['tag'];
        if (tag !== undefined) {
          this.tags.push(tag);
        }

        this.getWishList();
      },
      error => {

        this.appComponent.showError(error, "Error")
      });
  }

  getWishList(withSpinner = true) {
       this.spinner.show();

    this.wishListService.getWishList(this.category, this.login, this.tags).subscribe(
      itemList => {
        this.items = itemList;
          this.spinner.hide();
      }, error => {
        this.appComponent.showError(error, "Error")
         this.spinner.hide();
      });
  }

  getActionBackground(item: Item) {
    if (item.ownerId !== this.profile.id) {
      return 'item-action-add';
    } else {
      return 'item-action-del';
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

  bookWishItem(item: Item) {
    item.bookerId = this.profile.id;
    this.spinner.show();
    this.wishService.bookWishItem(item).subscribe(itemBooked => {

      //delete one item
      const index = this.items.indexOf(item);
      if (index !== -1) {
        this.items[index] = itemBooked;
      }

      this.spinner.hide();

      this.showSuccess('Wish item was successfully booked', 'Attention!');
    }, error => {
      this.spinner.hide();
      this.appComponent.showError(error, "Error")
    });
  }

  unbookWishItem(item: Item) {
    this.spinner.show();
    this.wishService.unbookWishItem(item).subscribe(itemUnBooked => {

      //delete one item
      const index = this.items.indexOf(item);
      if (index !== -1) {
        this.items[index] = itemUnBooked;
      }

      this.spinner.hide();

      this.showSuccess('Wish item was successfully deleted', 'Attention!');
    }, error => {
      this.appComponent.showError(error, "Error")
      this.spinner.hide();
    });
  }

  // Add Item

  addWishItem(item: Item) {
    let newItem = Object.assign({}, item);

    newItem.ownerId = this.profile.id;
    newItem.dueDate = this.dueDate + ' 00:00:00';
    newItem.priority = this.priority;

    this.spinner.show();
    this.wishService.addExistWishItem(newItem).subscribe(newItem => {
      this.spinner.hide();
      const index = this.items.indexOf(item);
      if (index !== -1) {
        this.items.splice(index, 1)
      }

      this.showSuccess('Wish item was successfully added', 'Attention!');
    }, error => {
      this.appComponent.showError(error, "Error")
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

  }

  deleteWishItem(item: Item) {
    this.spinner.show();
    this.wishService.deleteWishItem(item).subscribe(deletedItem => {
      this.spinner.hide();
      const index = this.items.indexOf(item);
      if (index !== -1) {
        this.items.splice(index, 1)
      }

      this.showSuccess('Wish item was successfully deleted', 'Attention!');
    }, error => {
      this.appComponent.showError(error, "Error")
      this.spinner.hide();
    });
  }

  tagsInputSubscriber() {
    this.queryTagField.valueChanges
      .debounceTime(400)
      .distinctUntilChanged()
      .subscribe(queryField => {
        if (this.tag !== undefined && this.tag !== '') {
          this.wishListService.getQueryTagList(queryField)
            .subscribe((queryTags) => {
              this.queryTags = queryTags;
            }, error => {
              this.showError(error, 'Error');
            })
        }
      }, error => {
          this.spinner.hide();
        this.appComponent.showError(error, "Error")
      });
  }

  addSearchTag(tag: string = this.tag) {
    if (tag.length > 2 && tag.length < 31 && /^[_A-Za-z0-9]*$/.test(tag) && this.tags.length < 8) {
      this.tags.push(tag);
      this.tag = '';
      this.getWishList();
    }
  }

  deleteSearchTag(tag: string) {
    const index = this.tags.indexOf(tag);
    if (index !== -1) {
      this.tags.splice(index, 1);
      this.getWishList();
    }
  }

  details(item: Item) {
    if(this.category === WishListComponent.OWN_CATEGORY) {
      this.router.navigate([`./${this.login}/wishes/${item.itemId}`]);
    } else if(this.category === WishListComponent.BOOKINGS_CATEGORY && item.ownerId !== 0) {
      this.spinner.show();
      this.accountService.getLoginById(item.ownerId).subscribe(
        login => {
          this.spinner.hide();
          this.router.navigate([`./${login}/wishes/${item.itemId}`]);
        }, error => {
          this.spinner.hide();
          this.router.navigate([`./${this.profile.login}/wishes/${item.itemId}`]);
        }
      );
    } else {
      this.router.navigate([`./no/wishes/${item.itemId}`]);
    }
  }
}
