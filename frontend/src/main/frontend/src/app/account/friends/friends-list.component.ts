import {Component} from "@angular/core";
import {FriendService} from "./friend.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'friends-list',
  templateUrl: './friends-list.component.html',
  styleUrls: ['./friends-list.component.css']
})

export class FriendsListComponent {
  newFriendName: string;
  state:string="friends";
  users = [{'id': 1, 'name': "User1", 'confirmed':true},
    {'id': 2, 'name': "User2", 'confirmed':false},
    {'id': 3, 'name': "User3", 'confirmed':true}];

  constructor(private friendService: FriendService) {
    //this.friendService.getFriends().subscribe((friends) => this.users = friends);
  }

  addFriend(form: NgForm) {
    this.friendService.addFriend(form.form.value.newFriendName).subscribe();
    this.newFriendName="";
  }
}
