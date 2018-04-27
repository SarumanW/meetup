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

  constructor(private folderService: FolderService,
              private route: ActivatedRoute,
              private router: Router,
              private spinner: NgxSpinnerService) {

  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.folderId = params['folderId'];
    });
    this.getEvents();
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }

  getEvents() {
    this.spinner.show();

    this.folderService.getEvents(this.folderId)
      .subscribe(events => {
        this.events = events;
        this.spinner.hide();
      })
  }

  openEvent(event: Evento) {
    this.router.navigate(["/" + this.profile.login + "/folders/" + event.folderId + "/" + event.eventId])
  }

}
