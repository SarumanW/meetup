import {Component, Input} from "@angular/core";
import {TestFriend} from "./test.friend";
import {FriendService} from "../friend.service";

@Component({
  selector: 'friend',
  templateUrl: './friend.component.html',
  styleUrls: [ './friend.component.css' ]
})

export class FriendComponent {
  @Input() user: TestFriend;

  constructor(private friendService: FriendService){
  }

  deleteFriend(id: number){
    this.friendService.deleteFriend(id).subscribe();
  }
}
