import {Component, OnInit} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {FriendService} from "../friends/friend.service";

@Component({
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit {

  state: string = "profile";
  profile: Profile;
  loggedUser: boolean;
  user: string;

  isFriend: boolean;
  isConfirmed: boolean;

  constructor(private accountService: AccountService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private friendService: FriendService) {
  }

  ngOnInit() {
    this.spinner.show();

    this.route.params.subscribe(params => {
      this.user = params['login']
    });

    this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === this.user;

    this.accountService.profile(this.user).subscribe(
      (profile) => {
        this.profile = profile;
        this.spinner.hide();
      });
    this.getFriends();
  }

  //TODO chang button depending on user
  getFriends() {
    this.friendService.getFriends(this.user).subscribe((friends) => {
      this.accountService.profile(JSON.parse(localStorage.getItem('currentUser')).login)
        .subscribe((user) => {
          if (friends.indexOf(user) != -1) {
            this.isFriend = true;
          } else {
            this.isFriend = false;
          }
          // console.log(this.isFriend);
          // console.log(friends);
        });
    });
    this.friendService.getFriendsRequests().subscribe((requests) => {
      if (requests.include(this.profile)) {
        this.isConfirmed = true;
      }
      console.log(this.isConfirmed);
      console.log(requests);
      console.log(this.profile);
    });
  }
}
