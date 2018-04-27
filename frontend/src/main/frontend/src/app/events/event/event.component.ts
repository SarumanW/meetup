import { Component, OnInit } from '@angular/core';
import {EventService} from "../event.service";
import {ActivatedRoute} from "@angular/router";
import {Evento} from "../event";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {

  eventId : number;
  folderId : number;
  eventt : Evento;
  currentUserId : number;
  currentUserLogin : string;
  alreadyHasParticipant : boolean;
  loginInput : string = "";
  state:string="folders";

  constructor(private eventService : EventService,
              private route: ActivatedRoute,
              private toastr: ToastrService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.eventId = params['eventId'];
      this.folderId = params['folderId'];
      this.currentUserId = JSON.parse(localStorage.currentUser).id;
      this.currentUserLogin = JSON.parse(localStorage.currentUser).login;
      this.alreadyHasParticipant = false;
    }, error => {
      this.showError('Unsuccessful event loading', 'Loading error');
    });
    this.getEvent();
  }

  getEvent() {
    this.eventService.getEvent(this.eventId).
    subscribe(eventt =>{
      this.eventt = eventt;
    }, error => {
      this.showError('Unsuccessful event loading', 'Loading error');
    })
  }

  showError(message : string, title: string) {
    this.toastr.error(message, title, {
      timeOut: 3000,
      positionClass: 'toast-bottom-left',
      closeButton: true
    });
  }

  addParticipant(name) {

    // this.eventt.participants.forEach(profile => {
    //   if (profile.login === this.currentUserLogin) {
    //     this.alreadyHasParticipant = true;
    //     break;
    //   }
    // }, this);
    console.log(name.value);
    console.log(this.loginInput);
    console.log(this.currentUserLogin);
    if (this.currentUserLogin !== name.value) {
      this.eventService.addParticipant(this.loginInput, this.eventId)
        .subscribe( participant => {
          this.eventt.participants.push(participant);
        }, error => {
          this.showError('Participant already exists', 'Adding error');
        });
    } else {
      this.showError('Unsuccessful participant adding', 'Adding error');
    }

  }

}
