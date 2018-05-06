import {Component, OnInit} from '@angular/core';
import {EventService} from "../event.service";
import {ActivatedRoute, Router} from "@angular/router";
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
  lat: number;
  lng: number;
  currentDate: string;
  datee: string;
  time: string;
  tempType: string;
  shouldShow: boolean;
  hasParticipant: boolean;
  type: string;

  constructor(private eventService: EventService,
              private route: ActivatedRoute,
              private toastr: ToastrService,
              private spinner: NgxSpinnerService,
              private router: Router) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.eventId = params['eventId'];
      this.folderId = params['folderId'];
      this.type = params['type'];
      this.currentUserId = JSON.parse(localStorage.currentUser).id;
      this.currentUserLogin = JSON.parse(localStorage.currentUser).login;
      this.alreadyHasParticipant = false;
      this.getCurrentDate();
      this.time = "00:00";
      this.shouldShow = false;
      this.datee = this.currentDate;
    }, error => {
      this.showError('Unsuccessful event loading', 'Loading error');
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
      this.tempType = eventt.eventType;
      this.spinner.hide();
    }, error => {
      this.spinner.hide();
      this.showError('Unsuccessful event loading', 'Loading error');
    })
  }

  showError(message: string, title: string) {
    this.toastr.error(message, title, {
      timeOut: 3000,
      positionClass: 'toast-top-right',
      closeButton: true
    });
  }

  showSuccess(message: string, title: string) {
    this.toastr.info(message, title, {
      timeOut: 3000,
      positionClass: 'toast-top-right',
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
          this.loginInput = "";
          this.spinner.hide();
          this.showSuccess('Participant was successfully added', 'Attention!');
        }, error => {
          this.showError('Unsuccessful participant adding', 'Adding error');
          this.spinner.hide();
        });
    } else {
      this.showError('Participant already exists', 'Adding error');
      this.spinner.hide();
    }

  }

  formatDate() {
    if (this.shouldShow) {
      console.log(this.datee);
      console.log(this.time);
      console.log(this.eventt.periodicity);
      this.eventt.eventDate = this.datee + " " + this.time + ":00";
    }
  }

  getCurrentDate() {
    let date =  new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    this.currentDate =  year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    console.log(this.currentDate);
  }

  updateEvent() {
    this.spinner.show();
    this.eventService.updateEvent(this.eventt).subscribe(
      updated => {
        this.showSuccess('Event is successfully updated', 'Success!');
        this.spinner.hide();
      }, error => {
        this.showError('Can not update event', 'Attention!');
        this.spinner.hide();
      }
    );
  }

  convertEvent() {

    if (this.eventt.eventType === 'EVENT') {
      if (this.eventt.participants.length !== 0) {
        this.deleteParticipants();
      }
    }

    this.eventt.eventType = this.tempType;

    this.updateEvent();
    this.shouldShow = false;
  }

  convertToDraft() {
    this.eventt.isDraft = !this.eventt.isDraft;
    this.tempType = this.eventt.eventType;
    this.updateEvent();
    this.shouldShow = false;
    console.log(this.eventt);
  }

  convertToPrivate() {

    if (this.eventt.eventType === 'NOTE') {
      this.shouldShow = true;
    }

    this.tempType = 'PRIVATE_EVENT';

  }

  convertToPublic() {

    if (this.eventt.eventType === 'NOTE') {
      this.shouldShow = true;
    }

    this.eventt.participants = [];
    this.tempType = 'EVENT';
  }

  convertToNote() {
    this.tempType = 'NOTE';
  }

  deleteParticipants() {
    this.spinner.show();
    this.eventService.deleteParticipants(this.eventt).subscribe(
      updated => {
        this.showSuccess('Participants removed successfully', 'Success!');
        this.eventt.participants = [];
        this.spinner.hide();
      }, error => {
        this.showError('Can not delete participants', 'Error!');
        this.spinner.hide();
      }
    );
  }

  deleteEvent() {
    this.eventService.deleteEvent(this.eventt).subscribe(
      deleted => {
        this.showSuccess('Event removed successfully', 'Success!');
        this.spinner.hide();
        this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId])
      }, error => {
        this.showError('Can not delete event', 'Error!');
        this.spinner.hide();
      }
    );
  }

  deleteParticipant(login: any) {

    this.spinner.show();

    let deletedProfileIndex;

    this.hasParticipant = false;

    if (this.eventt.participants !== null && this.eventt.participants.length !== 0) {
      for (let profile of this.eventt.participants) {
        if (profile.login === login.value) {
          this.hasParticipant = true;
          deletedProfileIndex = this.eventt.participants.indexOf(profile, 0);
          break;
        }
      }
    }

    if (this.currentUserLogin !== login.value && this.hasParticipant) {
      this.eventService.deleteParticipant(this.eventt, login.value).subscribe(
        deleted => {
          this.showSuccess(deleted.toString(), 'Success!');
          this.eventt.participants.splice(deletedProfileIndex, 1);
          this.spinner.hide();
        }, error => {
          this.showError('Participant with this login does not exist', 'Error!');
          this.spinner.hide();
        }
      );
    } else {
      this.showError('Participant with this login does not exist', 'Error!');
      this.spinner.hide();
    }

    login.value="";
  }

  editEvent() {
    this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId + "/" +
    this.type + "/" + this.eventId + "/edit"]);
  }

}
