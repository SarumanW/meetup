import {Component, Input, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {ActivatedRoute} from "@angular/router";
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
  relation: string;

  eventDate: any;
  currentDate: string;
  eventName: string;

  constructor(private accountService: AccountService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private friendService: FriendService,
              private appComponent: AppComponent) {
  }

  ngOnInit() {
    this.spinner.show();
    this.route.params.subscribe(params => {
      this.accountService.profileWithEvent(params['login']).subscribe(
        (profile) => {
          this.profile = profile;
          this.eventName = this.profile.pinedEventName;
          this.eventDate = this.profile.pinedEventDate;
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

  showCountdown() {
    let givenDate: any = new Date(this.eventDate);
    let now: any = new Date();
    let dateDifference: any = givenDate - now;

    return dateDifference >= 0;
  }

  getCurrentDate() {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    this.currentDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + day;
  }


  update() {
    this.friendService.getRelation(this.profile.id).subscribe((response) => {
    }, (error) => {
      this.friendService.getFriends(this.profile.login).subscribe((friendsList) => {
        this.relation = error.error.text;
        this.friendCount = friendsList.length;
      });
    });
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
