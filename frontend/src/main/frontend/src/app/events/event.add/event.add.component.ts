import { Component, OnInit } from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute} from "@angular/router";
import {Evento} from "../event";
import {EventAddService} from "../event.add.service";
import {ImageUploadService} from "../image.upload.service";

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

  constructor(private route: ActivatedRoute,
              private toastr: ToastrService,
              private spinner: NgxSpinnerService,
              private eventAddService: EventAddService,
              private uploadService: ImageUploadService) { }

  ngOnInit() {
    this.eventt = new Evento;

    this.route.params.subscribe(params => {
      this.folderId = params['folderId'];
    }, error => {
      this.showError('Unsuccessful parameters loading', 'Loading error');
    });

    this.getCurrentDate();
    this.resetEvent();
    this.fileRegexp = new RegExp('^.*\\.(jpg|JPG|gif|GIF|png|PNG)$');
    this.errorFileFormat = true;
  }

  resetEvent() {
    this.eventt.folderId = this.folderId;
    this.eventt.ownerId = JSON.parse(localStorage.currentUser).id;
    this.datee = this.currentDate;
    this.eventt.eventType = "EVENT";
    this.eventt.name = "";
    this.eventt.description = "";
    this.eventt.eventDate = "";
    this.eventt.periodicity = "";
    this.eventt.place = "";
    this.time = "00:00";
    this.lat = 50.447011182312195;
    this.lng = 30.456780195127067;
  }

  formatDate() {
    console.log(this.datee);
    console.log(this.time);
    console.log(this.eventt.periodicity);
    this.eventt.eventDate = this.datee + " " + this.time + ":00";
  }

  getCurrentDate() {
    let date =  new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    this.currentDate =  year + "-" + (month < 10 ? "0" + month : month) + "-" + day;
    console.log(this.currentDate);
  }

  addDraft() {
    this.eventt.isDraft = true;
    this.addEntity();
  }

  //TODO move to general component
  showError(message: string, title: string) {
    this.toastr.error(message, title, {
      timeOut: 3000,
      positionClass: 'toast-bottom-left',
      closeButton: true
    });
  }

  showSuccess() {
    this.toastr.info('Event was successfully added', 'Attention!', {
      timeOut: 3000,
      positionClass: 'toast-bottom-left',
      closeButton: true
    });
  }

  addEntity() {
    this.spinner.show();
    this.formatDate();
    this.eventt.place = this.lat + " " + this.lng;
    this.eventAddService.addEvent(this.eventt).subscribe(eventt => {
      this.spinner.hide();
      this.showSuccess();
      this.resetEvent();
    }, error => {
      this.showError('Unsuccessful event adding', 'Adding error');
      this.spinner.hide();
    });
  }

  addEvent() {
    console.log("addEvent");
    this.eventt.isDraft = false;
    this.addEntity();
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
    let filename: string = this.selectedFiles.item(0).name.toLowerCase();
    if (!this.fileRegexp.test(filename)) {
      this.showError("Incorrect file format " + this.selectedFiles.item(0).name, 'File format error');
      this.errorFileFormat = true;
    } else {
      this.errorFileFormat = false;
    }
  }

  upload() {
    this.spinner.show();
    this.imageLoaded = false;

    this.currentFileUpload = this.selectedFiles.item(0);
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      console.log(event);
        this.imageLoaded = true;
        this.eventt.imageFilepath = event;
        console.log(this.eventt.imageFilepath);
      this.spinner.hide();
    }, error => {
      this.showError(error, 'Upload failed');
      this.spinner.hide();
    });

    this.selectedFiles = undefined;
  }

  placeMarker(event){
    console.log(event.coords.lat);
    console.log(event.coords.lng);
    this.lng = event.coords.lng;
    this.lat = event.coords.lat;
  }

}
