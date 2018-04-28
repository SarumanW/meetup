import {Component, OnInit} from '@angular/core';
import {EventService} from "../event.service";
import {ActivatedRoute} from "@angular/router";
import {Evento} from "../event";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {

  eventId: number;
  folderId: number;
  eventt: Evento;
  currentUserId: number;
  currentUserLogin: string;
  alreadyHasParticipant: boolean;
  loginInput: string = "";
  state: string = "folders";

  constructor(private eventService: EventService,
              private route: ActivatedRoute,
              private toastr: ToastrService,
              private spinner: NgxSpinnerService) {
  }

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
    this.spinner.show();

    this.eventService.getEvent(this.eventId).subscribe(eventt => {
      this.eventt = eventt;
      this.spinner.hide();
    }, error => {
      this.spinner.hide();
      this.showError('Unsuccessful event loading', 'Loading error');
    })
  }

  showError(message: string, title: string) {
    this.toastr.error(message, title, {
      timeOut: 3000,
      positionClass: 'toast-bottom-left',
      closeButton: true
    });
  }

  showSuccess() {
    this.toastr.info('Parcicipant was successfully added', 'Attention!', {
      timeOut: 3000,
      positionClass: 'toast-bottom-left',
      closeButton: true
    });
  }

  addParticipant(name) {
    this.spinner.show();

    this.alreadyHasParticipant = false;

    if (this.eventt.participants !== null) {
      for (let profile of this.eventt.participants) {
        if (profile.login === this.loginInput) {
          this.alreadyHasParticipant = true;
          break;
        }
      }
    } else {
      this.eventt.participants = [];
    }

    if (this.currentUserLogin !== name.value && !this.alreadyHasParticipant) {
      this.eventService.addParticipant(this.loginInput, this.eventId)
        .subscribe(participant => {
          this.eventt.participants.push(participant);
          this.spinner.hide();
          this.showSuccess();
        }, error => {
          this.showError('Unsuccessful participant adding', 'Adding error');
          this.spinner.hide();
        });
    } else {
      this.showError('Participant already exists', 'Adding error');
      this.spinner.hide();
    }

  }

}
