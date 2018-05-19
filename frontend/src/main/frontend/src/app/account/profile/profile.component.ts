import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {FriendService} from "../friends/friend.service";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-event',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  encapsulation: ViewEncapsulation.None
})

export class ProfileComponent implements OnInit {

  state: string = "profile";
  profile: Profile;
  loggedUser: boolean;
  friendCount: number;
  isFriend: boolean;
  isConfirmed: boolean;
  currentDate: string;
  eventName: string;
  eventDate:string;

  constructor(private accountService: AccountService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private friendService: FriendService,
              private appComponent: AppComponent) {
    this.profile = new Profile();
  }



  ngOnInit() {
    this.spinner.show();
    this.route.params.subscribe(params => {
      this.accountService.profileWithEvent(params['login']).subscribe(
        (profile) => {
          this.profile = profile;
          this.eventName = this.profile.pinedEventName
          this.eventDate = this.profile.pinedEventDate
          this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === this.profile.login;
          this.update();
          this.spinner.hide();
        },error => {
          this.appComponent.showError(error, 'Error');
          this.spinner.hide();
        }
      );

    });
    this.getCurrentDate();
  }

  getCurrentDate() {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    this.currentDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + day;
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
