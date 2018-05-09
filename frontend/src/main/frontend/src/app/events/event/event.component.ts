import {Component, OnInit, ViewChild} from '@angular/core';
import {EventService} from "../event.service";
import {ActivatedRoute} from "@angular/router";
import {Evento} from "../event";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";
import {AppComponent} from "../../app.component";

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
  lat: number;
  lng: number;

  @ViewChild(AppComponent) childComponent: AppComponent
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
      if (error.status === 418) {
        this.childComponent.showError('The server encountered an error but still retry your request. Please wait..', 'Server error!');
      }else {
        this.childComponent.showError('Unsuccessful event loading', 'Loading error');
      }
    });
    this.getEvent();
  }

  getEvent() {
    this.spinner.show();

    this.eventService.getEvent(this.eventId).subscribe(eventt => {
      this.eventt = eventt;
      let coordinates = this.eventt.place.split(" ");
      console.log(this.eventt.eventType);
      this.lat = +coordinates[0];
      this.lng = +coordinates[1];
      this.spinner.hide();
    }, error => {
      if (error.status === 418) {
        this.childComponent.showError('The server encountered an error but still retry your request. Please wait..', 'Server error!');
      } else {
        this.spinner.hide();
        this.childComponent.showError('Unsuccessful event loading', 'Loading error');
      }
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
          if (error.status === 418) {
            this.childComponent.showError('The server encountered an error but still retry your request. Please wait..', 'Server error!');
          }else {
            this.childComponent.showError('Unsuccessful participant adding', 'Adding error');
          }
          this.spinner.hide();
        });
    } else {
      this.childComponent.showError('Participant already exists', 'Adding error');
      this.spinner.hide();
    }

  }

}
