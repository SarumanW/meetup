import {Component, Input, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Folder} from "../folder";
import {FolderService} from "../folder.service";
import {Evento} from "../../events/event";

@Component({
  selector: 'app-folder',
  templateUrl: './folder.component.html',
  styleUrls: [ './folder.component.css' ]
})
export class FolderComponent implements OnInit {
  events : Evento[];

  constructor(private eventService : FolderService) {

  }

  ngOnInit() {
    this.getEvents();
  }

  getEvents(){
    this.eventService.getEvents(1).subscribe(events =>{
      this.events = events;
    })
  }

}
