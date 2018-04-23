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
  newFriendName: string;
  state: string = "friends";
  unconfirmedFriends: Profile[];
  friends: Profile[];

  constructor(private friendService: FriendService) {
    this.friendService.getFriendsRequests().subscribe((requests) => this.unconfirmedFriends = requests);
    this.friendService.getFriends().subscribe((friends) => this.friends = friends);
  }

  addFriend(form: NgForm) {
    this.friendService.addFriend(form.form.value.newFriendName).subscribe();
    this.newFriendName = "";
  }
}
