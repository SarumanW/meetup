import { Component } from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";

@Component({
  selector: 'app-success',
  templateUrl: './profile.component.html',
  styleUrls: [ './profile.component.css' ]
})

export class ProfileComponent {

  profile : Profile;

  constructor(private accountService: AccountService){
    this.profile = new Profile();
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));

    console.log("profile service working");

    this.accountService.profile(this.profile).subscribe(
      (data) => {
        this.profile = data;
      }
    )
  }

  logout(){
    localStorage.clear();
  }
}
