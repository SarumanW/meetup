import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute, Router} from "@angular/router";
import {Evento} from "../event";
import {ImageUploadService} from "../image.upload.service";
import {FormControl} from "@angular/forms";
import {MapsAPILoader} from "@agm/core";
import {} from '@types/googlemaps';
import {ChatService} from "../../chat/chat.service";
import {AppComponent} from "../../app.component";
import {EventService} from "../event.service";

@Component({
  selector: 'app-event.add',
  templateUrl: './event.add.component.html',
  styleUrls: ['./event.add.component.css']
})
export class EventAddComponent implements OnInit {

  folderId: number;
  eventt: Evento;
  userId: number;
  currentDate: string;
  datee: string;
  time: string;
  state: string = "folders";
  selectedFiles: FileList;
  currentFileUpload: File;
  fileRegexp: RegExp;
  errorFileFormat: boolean;
  imageLoaded: boolean;
  lat: number;
  lng: number;
  searchControl: FormControl;
  currentUserLogin: string;
  currentUserId: number;
  type: string = 'event';

  @ViewChild("search") searchElementRef: ElementRef;

  constructor(private route: ActivatedRoute,
              private appComponent: AppComponent,
              private toastr: ToastrService,
              private spinner: NgxSpinnerService,
              private uploadService: ImageUploadService,
              private mapsAPILoader: MapsAPILoader,
              private eventService: EventService,
              private ngZone: NgZone,
              private router: Router,
              private chatService: ChatService) { }

  ngOnInit() {
    this.spinner.show();
    this.eventt = new Evento;
    this.route.params.subscribe(params => {
      this.folderId = params['folderId'];
    }, error => {
      this.appComponent.showError('Unsuccessful parameters loading', 'Loading error');
    });

    this.getCurrentDate();
    this.resetEvent();
    this.currentUserLogin = JSON.parse(localStorage.currentUser).login;
    this.currentUserId = JSON.parse(localStorage.currentUser).id;
    this.fileRegexp = new RegExp('^.*\\.(jpg|JPG|gif|GIF|png|PNG)$');

    this.searchControl = new FormControl();
    this.setCurrentPosition();

    this.mapsAPILoader.load().then(() => {
      let autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement, {
        types: ["address"]
      });
      autocomplete.addListener("place_changed", () => {
        this.ngZone.run(() => {
          let place: google.maps.places.PlaceResult = autocomplete.getPlace();

          if (place.geometry === undefined || place.geometry === null) {
            return;
          }

          this.lat = place.geometry.location.lat();
          this.lng = place.geometry.location.lng();
        });
      });
    });
    this.spinner.hide()
  }

  setCurrentPosition() {
    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.lat = position.coords.latitude;
        this.lng = position.coords.longitude;
      });
    }
  }

  resetEvent() {
    this.eventt.folderId = this.folderId;
    this.eventt.ownerId = JSON.parse(localStorage.currentUser).id;
    this.datee = this.currentDate;
    this.eventt.eventType = "EVENT";
    this.eventt.name = "";
    this.eventt.description = "";
    this.eventt.eventDate = this.currentDate;
    this.eventt.periodicity = "ONCE";
    this.eventt.place = "";
    this.time = "23:59";
    this.lat = 50.447011182312195;
    this.lng = 30.456780195127067;
    this.errorFileFormat = false;
  }

  formatDate() {
    this.eventt.eventDate = this.datee + " " + this.time + ":00";
  }

  getCurrentDate() {
    let date =  new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    this.currentDate =  year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
  }

  addDraft() {
    this.eventt.isDraft = true;
    if (this.selectedFiles) {
      this.upload();
    } else {
      this.addEntity();
    }
  }

  showSuccess() {
    this.toastr.info('Event was successfully added', 'Attention!', {
      timeOut: 3000,
      positionClass: 'toast-top-right',
      closeButton: true
    });
  }

  addEntity() {
    this.spinner.show();
    this.formatDate();
    this.eventt.place = this.lat + " " + this.lng;
    this.eventService.addEvent(this.eventt).subscribe(eventt => {
      this.showSuccess();

      if (eventt.eventType === 'EVENT') {
        this.addChat(eventt);
      } else {
        this.spinner.hide();
        this.redirectToEvent(eventt);
      }

      this.resetEvent();
    }, error => {
      this.appComponent.showError('Unsuccessful event adding', 'Adding error');
      this.spinner.hide();
    });
  }

  addChat(eventt: Evento) {
    this.chatService.addChat(eventt.eventId).subscribe(
      chat => {
        this.spinner.hide();
        this.redirectToEvent(eventt);
      }, error => {
        this.spinner.hide();
        this.appComponent.showError("Can not create chat", 'Adding error');
        this.redirectToEvent(eventt);
      }
    )
  }

  redirectToEvent(eventt: Evento) {
    this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId + "/" +
    this.type + "/" + eventt.eventId]);
  }

  addEvent() {
    this.eventt.isDraft = false;
    if (this.selectedFiles) {
      this.upload();
    } else {
      this.addEntity();
    }
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
    let filename: string = this.selectedFiles.item(0).name.toLowerCase();
    this.errorFileFormat = !this.fileRegexp.test(filename);
  }

  upload() {
    this.spinner.show();
    this.imageLoaded = false;

    this.currentFileUpload = this.selectedFiles.item(0);
    this.uploadService.pushFileToStorage(this.currentFileUpload, this.currentUserId).subscribe(event => {
        this.imageLoaded = true;
        this.eventt.imageFilepath = event;
        this.addEntity();
    }, error => {
      this.appComponent.showError(error, 'Upload failed');
      this.spinner.hide();
    });

    this.selectedFiles = undefined;
  }

  placeMarker(event){
    this.lng = event.coords.lng;
    this.lat = event.coords.lat;
  }

}
