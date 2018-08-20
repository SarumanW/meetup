import {Component, Input} from "@angular/core";
import {FriendService} from "../friend.service";
import {Profile} from "../../profile";
import {FriendsListComponent} from "../friends.list.component";
import {AppComponent} from "../../../app.component";

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
               private appComponent:AppComponent){
  }

  deleteFriend(id: number){
    this.friendService.deleteFriend(id).subscribe((response)=>this.friendsList.getInfo(),
      error => this.appComponent.showError(error, "Error"));
  }
  confirmFriend(id: number){
    this.friendService.confirmFriend(id).subscribe((response)=>this.friendsList.getInfo(),
      error => this.appComponent.showError(error, "Error"));
  }
}
