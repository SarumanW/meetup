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
  id: string;
  private sub: any;


  constructor(private router: Router,
              private spinner: NgxSpinnerService,
              private uploadService: UploadFileService,
              private toastr: ToastrService,
              private wishListService: WishListService,
              private wishService: WishService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.id = params['itemId']
    });

    this.getItem(this.id);

    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }


  like() {

  }

  getItem(id: string) {
    this.spinner.show();

    this.wishService.getWishItem(id).subscribe(item => {
      this.item = item;
      this.spinner.hide();
    });
  }


  removeFromWishList() {

  }

  addToWishList() {

  }
}
