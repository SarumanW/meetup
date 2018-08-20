import {Component, Input} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {Router} from "@angular/router";
import {FriendService} from "../friends/friend.service";

@Component({
  selector: 'home-comp',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent{
  @Input() states: string;
  profile: Profile;
  friendRequests: number;

  constructor(private accountService: AccountService,
              private router: Router,
              private friendService: FriendService) {
    this.profile = new Profile();
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
    this.updateRequests();
  }

  logout() {
    localStorage.clear();
    this.router.navigate(["/login"]);
  }

  updateRequests() {
    this.friendService.getFriendsRequests().subscribe(requests => this.friendRequests = requests.length);
  }
}
