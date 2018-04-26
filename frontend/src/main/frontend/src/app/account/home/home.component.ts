import {Component, Input} from '@angular/core';
import {Profile} from "../profile";
import {AccountService} from "../account.service";
import {Router} from "@angular/router";

@Component({
  selector: 'home-comp',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent {
  @Input() states: string;
   profile: Profile;

   constructor(private accountService: AccountService,
               private router: Router) {
     this.profile = new Profile();

   }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }

  logout() {
    localStorage.clear();
  }
}
