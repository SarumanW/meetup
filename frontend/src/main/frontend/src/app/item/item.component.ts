import { Component, OnInit } from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {Router, ActivatedRoute} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {LoginAccount} from "../account/login.account";
import {AccountService} from "../account/account.service";
import {Item} from "../item";


@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.css']
})
export class ItemComponent implements OnInit {


  success: boolean;
  account: LoginAccount;
  errorMessage: string;
  item: Item;


  constructor(private router: Router,
              private spinner: NgxSpinnerService) {}

  ngOnInit() {
  }

  createItem(){

  }

  like(){

  }

}
