import {Component, Input} from "@angular/core";
import {TestFriend} from "./test.friend";
import {FriendService} from "../friend.service";
import {Profile} from "../../profile";

@Component({
  selector: 'friend',
  templateUrl: './friend.component.html',
  styleUrls: [ './friend.component.css' ]
})

export class FriendComponent {
  @Input() user: Profile;
  state:string="friends"
  constructor(private friendService: FriendService){
  }

  deleteFriend(id: number){
    this.friendService.deleteFriend(id).subscribe();
  }
}
