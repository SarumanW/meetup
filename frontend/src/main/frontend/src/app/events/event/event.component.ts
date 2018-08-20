import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {EventService} from "../event.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Evento} from "../event";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";
import {AppComponent} from "../../app.component";
import {ChatService} from "../../chat/chat.service";
import {FriendService} from "../../account/friends/friend.service";
import {FormControl} from "@angular/forms";
import {AccountService} from "../../account/account.service";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css'],
  encapsulation: ViewEncapsulation.None
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
  datee: any;
  time: string;
  ownerLogin: string;
  tempType: string;
  tempTypeShow: string;
  shouldShow: boolean;
  hasParticipant: boolean;
  type: string;
  isPinned:boolean;
  isParticipant: boolean;
  deleteParticipantQueryField: FormControl = new FormControl();
  addParticipantQueryField: FormControl = new FormControl();
  queryDeleteParticipants: string[] = [];
  queryUsers: string[] = [];
  place:string;
  ownerId:number;


  constructor(private eventService: EventService,
              private friendService: FriendService,
              private route: ActivatedRoute,
              private toastr: ToastrService,
              private spinner: NgxSpinnerService,
              private router: Router,
              private appComponent: AppComponent,
              private chatService: ChatService,
              private accountService: AccountService) {
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

      this.isPinned = (this.eventId === JSON.parse(localStorage.getItem('currentUser')).pinedEventId)
    }, error => {
      this.appComponent.showError('Unsuccessful event loading', 'Loading error');
    });
    this.getEvent();
    this.participantInputSubscriber();
    this.deleteParticipantInputSubscriber();
  }

  showCountdown() {
    let givenDate: any = new Date(this.eventt.eventDate);
    let now: any = new Date();
    let dateDifference: any = givenDate - now;

    return dateDifference >= 0;
  }

  getEvent() {
    this.spinner.show();

    this.eventService.getEvent(this.eventId).subscribe(eventt => {
      this.eventt = eventt;
      let coordinates = this.eventt.place.split(" ");
      this.lat = +coordinates[0];
      this.lng = +coordinates[1];
      this.tempType = eventt.eventType;
      this.isParticipantt();
      if (eventt.eventType === 'EVENT') {
        this.getChatIds(eventt);
      }
      this.accountService.getLoginById(eventt.ownerId).subscribe(
        login => {
          this.ownerLogin = login;
          this.spinner.hide();

      this.eventService.getPlace(eventt.place).subscribe(
        place => {
          this.place = place.results[0].formatted_address;
          this.spinner.hide();
        }, error => {
          this.spinner.hide();
          this.appComponent.showError('Unsuccessful event loading', 'Loading error');
        })
    }, error => {
          this.ownerLogin = null;
          this.spinner.hide();
    })
    }, error => {
      this.spinner.hide();
      this.appComponent.showError('Unsuccessful event loading', 'Loading error');
    });
  };

  isParticipantt() {
    if (this.eventt.participants !== null) {
      for (let profile of this.eventt.participants) {
        if (profile.id === this.currentUserId) {
          this.isParticipant = true;
          break;
        }
      }
    } else {
      this.eventt.participants = [];
    }
  }

  getChatIds(eventt: Evento) {
    this.chatService.getChatIds(eventt.eventId).subscribe(
      chat=> {
        this.eventt.privateChatId = chat.privateChatId;
        this.eventt.publicChatId = chat.publicChatId;
      }, error => {
        this.showError('Problem with chats loading','Attention!');
      }
    );
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

  pinEvent() {
    this.spinner.show();
    this.eventService.pinEvent(this.currentUserId, this.eventId).subscribe(
      (event: Evento) => {
        this.isPinned = !this.isPinned;
        let profile = JSON.parse(localStorage.currentUser);
        profile.pinedEventId = this.eventId;
        localStorage.setItem('currentUser', JSON.stringify(profile));
        this.showSuccess('Event is successfully pinned', 'Success!');
        this.spinner.hide();
      },
      error => {
        this.appComponent.showError('Can not pin event', 'Attention!');
        this.spinner.hide();
      }
    );
  }

  unpinEvent() {
    this.spinner.show();
    this.eventService.unpinEvent(this.currentUserId, this.eventId).subscribe(
      (event: Evento) => {
        this.isPinned = !this.isPinned;
        let profile = JSON.parse(localStorage.currentUser);
        profile.pinedEventId = 0;
        localStorage.setItem('currentUser', JSON.stringify(profile));
        this.showSuccess('Event is successfully unpinned', 'Success!');
        this.spinner.hide();
      },
      error => {
        this.appComponent.showError('Can not unpin event', 'Attention!');
        this.spinner.hide();
      }
    );
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
          this.appComponent.showError('Unsuccessful event loading', 'Loading error');
          this.spinner.hide();
        });
    } else {
      this.appComponent.showError('Participant already exists', 'Adding error');
      this.spinner.hide();
    }

  }

  formatDate() {
    if (this.shouldShow) {
      this.eventt.eventDate = this.datee + " " + this.time + ":00";
    }
  }

  getCurrentDate() {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    this.currentDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
  }

  updateEvent() {
    this.spinner.show();
    this.eventService.updateEvent(this.eventt).subscribe(
      updated => {
        this.showSuccess('Event is successfully updated', 'Success!');
        this.spinner.hide();
      }, error => {
        this.appComponent.showError('Can not update event', 'Attention!');
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
  }

  convertToPrivate() {

    if (this.eventt.eventType === 'NOTE') {
      this.shouldShow = true;
    }

    this.tempType = 'PRIVATE_EVENT';
    this.tempTypeShow = 'Private event'
  }

  convertToPublic() {

    if (this.eventt.eventType === 'NOTE') {
      this.shouldShow = true;
    }

    this.eventt.participants = [];
    this.tempType = 'EVENT';
    this.tempTypeShow = 'Public event'
  }

  convertToNote() {
    this.tempType = 'NOTE';
    this.tempTypeShow = 'Note'
  }

  deleteParticipants() {
    this.spinner.show();
    this.eventService.deleteParticipants(this.eventt).subscribe(
      updated => {
        this.showSuccess('Participants removed successfully', 'Success!');
        this.eventt.participants = [];
        this.spinner.hide();
      }, error => {
        this.appComponent.showError('Can not delete participants', 'Error!');
        this.spinner.hide();
      }
    );
  }

  deleteEvent() {
    this.spinner.show();
    this.eventService.deleteEvent(this.eventt).subscribe(
      deleted => {
        this.showSuccess('Event removed successfully', 'Success!');
        this.spinner.hide();
        this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId])
      }, error => {
        this.appComponent.showError('Can not delete event', 'Error!');
        this.spinner.hide();
      }
    );
  }

  deleteParticipant(login: string) {
    this.spinner.show();

    let deletedProfileIndex = -1;

    this.hasParticipant = false;

    if (this.eventt.participants !== null && this.eventt.participants.length !== 0) {
      for (let profile of this.eventt.participants) {
        if (profile.login === login) {
          this.hasParticipant = true;
          deletedProfileIndex = this.eventt.participants.indexOf(profile, 0);
          break;
        }
      }
    }
    this.loginInput = "";
    if (this.currentUserLogin !== login && deletedProfileIndex !== -1) {
      this.eventService.deleteParticipant(this.eventt, deletedProfileIndex).subscribe(
        deleted => {
          this.showSuccess(deleted.toString(), 'Success!');
          this.eventt.participants.splice(deletedProfileIndex, 1);
          this.spinner.hide();
        }, error => {
          this.appComponent.showError('Participant with this login does not exist', 'Error!');
          this.spinner.hide();
        }
      );
    } else {
      this.appComponent.showError('Participant with this login does not exist', 'Error!');
      this.spinner.hide();
    }

    login = "";
  }

  editEvent() {
    this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId + "/" +
    this.type + "/" + this.eventId + "/edit"]);
  }

  onPublicChat() {
    this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId + "/event/" + this.eventId +
    "/chat/" + this.eventt.publicChatId]);
  }

  onPrivateChat() {
    this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId + "/event/" + this.eventId +
    "/chat/" + this.eventt.privateChatId]);
  }


  goToProfile(member: string) {
    this.router.navigate(["/" + member + "/profile"]);
  }

  participantInputSubscriber() {
    this.addParticipantQueryField.valueChanges
      .debounceTime(400)
      .distinctUntilChanged()
      .subscribe(loginInput => {
        if (loginInput !== undefined && loginInput !== '') {
          this.friendService.getUsersByUsernamePart(this.loginInput, 'all')
            .subscribe((queryLogins) => {
              this.queryUsers = queryLogins;
            }, error => {
              this.showError(error, 'Error');
            })
        }
      }, error => {
        this.appComponent.showError(error, "Error")
      });
  }

  deleteParticipantInputSubscriber() {
    this.deleteParticipantQueryField.valueChanges
      .debounceTime(400)
      .distinctUntilChanged()
      .subscribe(input => {
        if (input !== undefined && input !== '') {
          this.queryDeleteParticipants = [];
          for (let participant of this.eventt.participants) {
            if(participant.login.includes(input)) {
              this.queryDeleteParticipants.push(participant.login);
            }
          }
        }
      }, error => {
        this.appComponent.showError(error, "Error")
      });

  }

}
