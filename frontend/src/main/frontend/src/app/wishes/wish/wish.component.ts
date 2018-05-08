import { Component, OnInit } from '@angular/core';
import {LoginAccount} from "../../account/login.account";
import {Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {Item} from "../../item";

@Component({
  selector: 'app-wish',
  templateUrl: './wish.component.html',
  styleUrls: ['./wish.component.css']
})
export class WishComponent implements OnInit {
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
