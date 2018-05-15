import {Component, OnInit} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {FriendService} from "../friends/friend.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AppComponent} from "../../app.component";

@Component({
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit {

  state: string = "profile";
  profile: Profile;
  loggedUser: boolean;

  relation: string;
  friendCount: number;
  message: string;

  constructor(private accountService: AccountService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private friendService: FriendService,
              private http: HttpClient,
              private appComponent:AppComponent) {
  }

  ngOnInit() {
    this.spinner.show();
    this.route.params.subscribe(params => {
      this.accountService.profile(params['login']).subscribe(
        (profile) => {
          this.profile = profile;
          this.update();
          this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === this.profile.login;
          this.spinner.hide();
        });
    });
  }

  update() {
    this.friendService.getRelation(this.profile.id).subscribe(response => {
    }, error => {
      this.friendService.getFriends(this.profile.login).subscribe((friendsList) => {
        this.relation = error.error.text;
        this.friendCount = friendsList.length;
      });
    });
  }

  addFriend(login: string) {
    this.friendService.addFriend(login).subscribe((message) => {
        this.message = message
        this.spinner.hide();
      },
      (error) => {
        if (error.status === 200) {
          this.message = error.error.text;
        } else {
          this.message = error.error;
        }
        this.update();
        this.spinner.hide();
      });

  }

  deleteFriend(id: number) {
    this.friendService.deleteFriend(id).subscribe((result) => {
      this.update();
    });
  }

  confirmFriend(id: number) {
    this.friendService.confirmFriend(id).subscribe((result) => {
      this.update();
    });
  }
}
