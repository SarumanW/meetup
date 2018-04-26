import { Component, OnInit } from '@angular/core';
import {EventService} from "../event.service";
import {ActivatedRoute} from "@angular/router";
import {Evento} from "../event";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {

  eventId : number;
  folderId : number;
  eventt : Evento;
  state:string="folders";

  constructor(private eventService : EventService,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.eventId = params['eventId'];
      this.folderId = params['folderId'];
    });
    this.getEvent();
  }

  getEvent() {
    this.eventService.getEvent(this.eventId).
    subscribe(eventt =>{
      this.eventt = eventt;
    })
  }

}
