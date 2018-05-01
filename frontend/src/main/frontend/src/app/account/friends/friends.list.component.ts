import {Component, OnInit} from "@angular/core";
import {FriendService} from "./friend.service";
import {NgForm} from "@angular/forms";
import {Profile} from "../profile";
import "rxjs/add/observable/timer";
import {NgxSpinnerService} from "ngx-spinner";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'friends-list',
  templateUrl: './friends.list.component.html',
  styleUrls: ['./friends.list.component.css']
})

export class FriendsListComponent implements OnInit {
  state: string = "friends";
  newFriendName: string;
  friends: Profile[] = [];
  unconfirmedFriends: Profile[] = [];
  message: string;
  loggedUser: boolean;
  user: string;

  constructor(private friendService: FriendService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.spinner.show();
    this.route.params.subscribe(params => {
      this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === params['login'];
      this.user = params['login']
      this.getInfo();
      this.spinner.hide();
    });
  }

  getInfo() {
    if (this.loggedUser) {
      this.friendService.getFriendsRequests()
        .subscribe((requests) => {
          this.unconfirmedFriends = requests
        });
    }

    this.route.params.subscribe(params => {
      this.friendService.getFriends(params['login'])
        .subscribe((friends) => {
          this.friends = friends
        });
    });
  }

  addFriend(form: NgForm) {
    this.spinner.show();

    this.friendService.addFriend(form.form.value.newFriendName)
      .subscribe(
        (message) => {
          this.message = message
          this.spinner.hide();
        },
        (error) => {
          if (error.status === 200) {
            this.message = error.error.text;
          } else {
            this.message = error.error;
          }
          this.spinner.hide();
        });

    this.newFriendName = "";
  }
}
