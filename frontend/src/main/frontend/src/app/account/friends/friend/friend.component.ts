import {Component, Input} from "@angular/core";
import {FriendService} from "../friend.service";
import {Profile} from "../../profile";
import {FriendsListComponent} from "../friends.list.component";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'friend',
  templateUrl: './friend.component.html',
  styleUrls: [ './friend.component.css' ]
})

export class FriendComponent {
  @Input() confirmed: boolean;
  @Input() user: Profile;
  @Input() loggedUser: boolean;
  state:string="friends"
  constructor(private friendService: FriendService,
              private friendsList: FriendsListComponent,
              private toastr: ToastrService){
  }

  deleteFriend(id: number){
    this.friendService.deleteFriend(id).subscribe((response)=>this.friendsList.getInfo());
  }
  confirmFriend(id: number){
    this.friendService.confirmFriend(id).subscribe((response)=>this.friendsList.getInfo());
  }

}
