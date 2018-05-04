import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Evento} from "../../events/event";
import {Profile} from "../../account/profile";

@Component({
  selector: 'app-folder',
  templateUrl: './folder.component.html',
  styleUrls: ['./folder.component.css']
})
export class FolderComponent implements OnInit {
  events: Evento[];
  folderId: number;
  state: string = "folders";
  profile: Profile;

  margins = {
    top: 25,
    bottom: 60,
    left: 20,
    width: 522
  };

  constructor(private route: ActivatedRoute,
              private router: Router) {

  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.folderId = params['folderId'];
    });

    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }

  openEventList(type : string) {
    this.router.navigate(["/" + this.profile.login + "/folders/" + this.folderId + "/" + type])
  }

  addEvent() {
    this.router.navigate(["/" + this.profile.login + "/event/add/" + this.folderId]);
  }

}
