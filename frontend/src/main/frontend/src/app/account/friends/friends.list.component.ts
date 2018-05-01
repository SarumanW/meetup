import {Component, OnInit} from "@angular/core";
import {FriendService} from "./friend.service";
import {FormControl} from "@angular/forms";
import {Profile} from "../profile";
import "rxjs/add/operator/debounceTime";
import "rxjs/add/operator/distinctUntilChanged";
import "rxjs/add/operator/switchMap";

@Component({
  selector: 'friends-list',
  templateUrl: './friends.list.component.html',
  styleUrls: ['./friends.list.component.css']
})

export class FriendsListComponent implements OnInit{
  state: string = "friends";
  newFriendName: string;
  friends: Profile[];
  unknownUsers : Profile[] = [];
  unconfirmedFriends: Profile[] = [];
  message: string;
  queryField: FormControl = new FormControl();

  constructor(private friendService: FriendService) {
    this.getInfo();
  }

  ngOnInit(){
    this.queryField.valueChanges
      .debounceTime(1000)
      .distinctUntilChanged()
      .subscribe(queryField =>{
        this.unknownUsers = [];
        this.friendService.getUnknownUsers(queryField)
          .subscribe((unknownUsers) => this.unknownUsers = unknownUsers)}
        );
  }
  getInfo(){
    this.friendService.getFriendsRequests().subscribe((requests) => this.unconfirmedFriends = requests);
    this.friendService.getFriends().subscribe((friends) => this.friends = friends);
  }

  addFriend(userName : string) {
    this.friendService.addFriend(userName).subscribe((message) => this.message = message);
    this.newFriendName = "";
  }

}
