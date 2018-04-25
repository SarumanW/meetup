import {Component} from "@angular/core";
import {FriendService} from "./friend.service";
import {NgForm} from "@angular/forms";
import {Profile} from "../profile";

@Component({
  selector: 'friends-list',
  templateUrl: './friends.list.component.html',
  styleUrls: ['./friends.list.component.css']
})

export class FriendsListComponent {
  state: string = "friends";
  newFriendName: string;
  friends: Profile[];
  unconfirmedFriends: Profile[];
  message: string;

  constructor(private friendService: FriendService) {
    this.getInfo();
  }

  getInfo(){
    this.friendService.getFriendsRequests().subscribe((requests) => this.unconfirmedFriends = requests);
    this.friendService.getFriends().subscribe((friends) => this.friends = friends);
  }

  addFriend(form: NgForm) {
    this.friendService.addFriend(form.form.value.newFriendName).subscribe((message) => this.message = message);
    this.newFriendName = "";

  }
}
