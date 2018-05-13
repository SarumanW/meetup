import {Component, OnInit} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {FriendService} from "../friends/friend.service";
import {FriendsListComponent} from "../friends/friends.list.component";
import {AppComponent} from "../../app.component";

@Component({
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit {

  state: string = "profile";
  profile: Profile;
  loggedUser: boolean;

  friendCount: number;
  isFriend: boolean;
  isConfirmed: boolean;

  constructor(private accountService: AccountService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private friendService: FriendService,
              private appComponent: AppComponent) {
    this.profile = new Profile();
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.accountService.profile(params['login']).subscribe(
        (profile) => {
          this.profile = profile;
          this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === this.profile.login;
          this.update();
        },error => {
          this.appComponent.showError(error, 'Upload failed');
        }
      );
    });
  }

  update() {
    this.spinner.show();
    this.getButton();
    this.spinner.hide();
  }

  // TODO move it to the backend
  getButton() {
    this.friendService.getFriends(this.profile.login).subscribe((friends) => {
      this.accountService.profile(JSON.parse(localStorage.getItem('currentUser')).login)
        .subscribe((user) => {
          if (friends.length === 0) {
            this.isFriend = false;
          }
          for (let i = 0; i < friends.length; i++) {
            if (user.id === friends[i].id) {
              this.isFriend = true;
              break;
            }
            else {
              this.isFriend = false;
            }
          }
          this.friendCount = friends.length;
        },error => {
            this.appComponent.showError(error, 'Error');
          }
        );
    },error => {
        this.appComponent.showError(error, 'Error');
      }
    );
    this.friendService.getFriendsRequests().subscribe((requests) => {
      if (requests.length === 0) {
        this.isConfirmed = true;
      }
      for (let i = 0; i < requests.length; i++) {
        if (this.profile.id === requests[i].id) {
          this.isConfirmed = false;
          break;
        }
        else {
          this.isConfirmed = true;
        }
      }
    },error => {
        this.appComponent.showError(error, 'Error');
      }
    );
  }

  addFriend(login: string) {
    this.friendService.addFriend(login).subscribe((result) => {
      this.update()
    },error => {
        this.appComponent.showError(error, 'Error');
      }
    );
  }

  deleteFriend(id: number) {
    this.friendService.deleteFriend(id).subscribe((result) => {
      this.update()
    },error => {
        this.appComponent.showError(error, 'Error');
      }
    );
  }

  confirmFriend(id: number) {
    this.friendService.confirmFriend(id).subscribe((result) => {
      this.update()
    },error => {
        this.appComponent.showError(error, 'Error');
      }
    );
  }
}
