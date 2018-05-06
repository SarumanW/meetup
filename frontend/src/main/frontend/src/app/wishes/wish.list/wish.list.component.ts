import { Component, OnInit } from '@angular/core';
import {Item} from "../item";
import {WishListService} from "../wish.list.service";
import {Profile} from "../../account/profile";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-wish-list',
  templateUrl: './wish.list.component.html',
  styleUrls: ['./wish.list.component.css']
})
export class WishListComponent implements OnInit {
  items: Item[];
  state: string = "wishes";
  profile: Profile;

  constructor(private wishListService: WishListService,
              private spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.spinner.show();

    this.getWishList();

    this.spinner.hide();

    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }

  getWishList(): void {
    this.spinner.show();

    this.wishListService.getWishList().subscribe(
      itemList => {
        this.items = itemList;
        this.spinner.hide();
      })
  }

}
