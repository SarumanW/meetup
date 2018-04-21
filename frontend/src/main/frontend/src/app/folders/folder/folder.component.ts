import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Folder} from "../folder";
import {FolderService} from "../folder.service";
import {Evento} from "../../events/event";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-folder',
  templateUrl: './folder.component.html',
  styleUrls: [ './folder.component.css' ]
})
export class FolderComponent implements OnInit {
  events : Evento[];
  folderId : number;

  constructor(private folderService : FolderService,
              private route: ActivatedRoute) {

  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.folderId = params['folderId'];
    });
    this.getEvents();
  }

  getEvents(){
    this.folderService.getEvents(this.folderId).
    subscribe(events =>{
      this.events = events;
    })
  }

}
