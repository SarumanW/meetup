import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Folder} from "../folder";
import {FolderService} from "../folder.service";
import {Evento} from "../../events/event";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Profile} from "../../account/profile";
import {NgxSpinnerService} from "ngx-spinner";

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

}
