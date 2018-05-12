import {Component, OnInit} from "@angular/core";
import {FriendService} from "./friend.service";
import {FormControl} from "@angular/forms";
import {Profile} from "../profile";
import "rxjs/add/observable/timer";
import {NgForm} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/debounceTime";
import "rxjs/add/operator/distinctUntilChanged";
import "rxjs/add/operator/switchMap";

@Component({
  selector: 'friends-list',
  templateUrl: './friends.list.component.html',
  styleUrls: ['./friends.list.component.css']
})

export class FriendsListComponent implements OnInit {
  state: string = "friends";
  newFriendName: string;
  friends: Profile[];
  unknownUsers: Profile[] = [];
  unconfirmedFriends: Profile[] = [];
  message: string;
  loggedUser: boolean;
  user: string;
  queryField: FormControl = new FormControl();

  constructor(private friendService: FriendService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.loggedUser = JSON.parse(localStorage.getItem('currentUser')).login === params['login'];
      this.user = params['login'];
      this.getInfo();
      this.queryField.valueChanges
        .debounceTime(1000)
        .distinctUntilChanged()
        .subscribe(queryField =>{
          this.unknownUsers = [];
          this.friendService.getUnknownUsers(queryField)
            .subscribe((unknownUsers) => this.unknownUsers = unknownUsers)}
        );
      this.spinner.hide();
    });
  }

  getInfo() {

    this.spinner.show();

    // if (this.loggedUser) {
    this.friendService.getFriendsRequests()
      .subscribe((requests) => {
        this.unconfirmedFriends = requests;
      });
    // }
    this.route.params.subscribe(params => {
      this.friendService.getFriends(params['login'])
      // this.friendService.getFriends()
        .subscribe((friends) => {
          this.friends = friends
          this.spinner.hide();
        });
    });
  }

  addFriend(login: string) {
    this.spinner.show();
    // console.log("adding friend " + form.form.value.newFriendName)
    // this.friendService.addFriend(form.form.value.newFriendName)
    this.friendService.addFriend(login)
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
