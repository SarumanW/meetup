import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {Evento} from "../event";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {ToastrService} from "ngx-toastr";
import {EventService} from "../event.service";
import {ImageUploadService} from "../image.upload.service";
import {FormControl} from "@angular/forms";
import {MapsAPILoader} from "@agm/core";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-event.edit',
  templateUrl: './event.edit.component.html',
  styleUrls: ['./event.edit.component.css']
})
export class EventEditComponent implements OnInit {

  eventId: number;
  folderId: number;
  currentUserId: number;
  eventt: Evento;
  lat: number;
  lng: number;
  datee: string;
  time: string;
  state: string = "folders";
  selectedFiles: FileList;
  currentFileUpload: File;
  fileRegexp: RegExp;
  errorFileFormat: boolean;
  imageLoaded: boolean;
  type: string;
  currentUserLogin: string;
  searchControl: FormControl;

  @ViewChild("searchh")
  searchElementRef: ElementRef;

  constructor(private eventService: EventService,
              private route: ActivatedRoute,
              private toastr: ToastrService,
              private spinner: NgxSpinnerService,
              private router: Router,
              private uploadService: ImageUploadService,
              private mapsAPILoader: MapsAPILoader,
              private ngZone: NgZone,
              private appComponent: AppComponent) { }

  ngOnInit() {
    this.spinner.show()

    this.route.params.subscribe(params => {
      this.eventId = params['eventId'];
      this.folderId = params['folderId'];
      this.type = params['type'];
      this.currentUserId = JSON.parse(localStorage.currentUser).id;
      this.currentUserLogin = JSON.parse(localStorage.currentUser).login;
    }, error => {
      this.appComponent.showError(error, 'Loading error');
    });
    this.getEvent();
    this.errorFileFormat = false;
    this.fileRegexp = new RegExp('^.*\\.(jpg|JPG|gif|GIF|png|PNG)$');

    this.searchControl = new FormControl();

    this.setCurrentPosition();

    this.mapsAPILoader.load().then(() => {
      let autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement,
        { types:['address']
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
  }

  setCurrentPosition() {
    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.lat = position.coords.latitude;
        this.lng = position.coords.longitude;
      });
    }
  }

  getEvent() {
    this.spinner.show();

    this.eventService.getEvent(this.eventId).subscribe(eventt => {
      this.eventt = eventt;
      let coordinates = this.eventt.place.split(" ");
      this.lat = +coordinates[0];
      this.lng = +coordinates[1];
      let loadDate =this.eventt.eventDate.split(" ");
      this.datee = loadDate[0];
      this.time = loadDate[1].split(".")[0];
      this.spinner.hide();
    }, error => {
      this.spinner.hide();
      this.appComponent.showError(error, 'Loading error');
    })
  }

  formatDate() {
      this.eventt.eventDate = this.datee + " " + this.time;
  }

  update() {
    if (this.selectedFiles) {
      this.upload();
    } else {
      this.updateEvent();
    }
  }

  updateEvent() {
    this.spinner.show();
    this.formatDate();
    this.eventt.place = this.lat + " " + this.lng;
    this.eventService.updateEvent(this.eventt).subscribe(
      updated => {
        this.showSuccess('Event is successfully updated', 'Success!');
        this.spinner.hide();
        this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId + "/" + this.type + "/" + this.eventId]);
      }, error => {
        this.appComponent.showError(error, 'Attention!');
        this.spinner.hide();
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
      this.updateEvent();
      this.spinner.hide();
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
