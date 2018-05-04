import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Folder} from "../folder";
import {FolderListService} from "../folder.list.service";
import {FolderService} from "../folder.service";
import {Observable} from "rxjs/Observable";
import {ToastrService} from "ngx-toastr";
import {Profile} from "../../account/profile";
import {NgxSpinnerService} from "ngx-spinner";
import * as html2canvas from "html2canvas"
import * as jsPDF from "jspdf";
import {Evento} from "../../events/event";
import {EventService} from "../../events/event.service";

@Component({
  selector: 'app-folder',
  templateUrl: './folder.list.component.html',
  styleUrls: ['./folder.list.component.css']
})
export class FolderListComponent implements OnInit {

  folders: Folder[];
  selectedFolder: Folder;
  state: string = "folders";
  nameInput: string = "";
  profile: Profile;

  currentDate: string;
  startDate: string;
  endDate: string;
  periodEvents: Evento[];

  constructor(private folderListService: FolderListService,
              private spinner: NgxSpinnerService,
              private toastr: ToastrService,
              private eventService: EventService) {

    this.selectedFolder = new Folder;
  }

  ngOnInit() {
    this.getFoldersList();
    this.profile = JSON.parse(localStorage.getItem('currentUser'));

    this.getCurrentDate();
  }

  getCurrentDate() {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    this.currentDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + day;
  }

  formatDate() {
    this.startDate = this.startDate + " 00:00:00";
    this.endDate = this.endDate + " 00:00:00";
  }

  getFoldersList(): void {
    this.spinner.show();

    this.folderListService.getFoldersList().subscribe(
      folders => {
        this.folders = folders
        this.spinner.hide();
      })
  }

  getPeriodEvents(start: string, end: string): void {
    this.eventService.getEventsInPeriod(start, end).subscribe(
      events => {
        this.periodEvents = events;
      }
    )
  }

  addFolder(folderName) {
    this.spinner.show();

    let folder: Folder = new Folder();
    folder.name = folderName.value;
    folder.userId = JSON.parse(localStorage.currentUser).id;

    this.folderListService.addFolder(folder)
      .subscribe((res) => {
        folder.folderId = res.folderId;
        this.folders.push(folder);
        this.spinner.hide();
      });

    this.nameInput = null;
  }

  deleteFolder(folder) {
    let isSure = confirm("Are you sure?");

    if (isSure) {
      this.spinner.show();

      this.folderListService.deleteFolder(folder)
        .subscribe(() => {
            const index: number = this.folders.indexOf(folder);
            if (index !== -1) {
              this.folders.splice(index, 1);
            }

            this.spinner.hide();
            this.showSuccess();
          },
          () => {
            this.spinner.hide();
          });
    }
  }

  showSuccess() {
    this.toastr.info('Your events were moved to general folder', 'Attention!', {
      timeOut: 3000,
      positionClass: 'toast-bottom-left',
      closeButton: true
    });
  }

  downloadPlan() {

    this.formatDate();
    this.getPeriodEvents(this.startDate, this.endDate);

    console.log(this.periodEvents);

    // let doc = new jsPDF('p', 'pt', 'a4');
    //
    // let element = <HTMLScriptElement>document.getElementsByClassName("download")[0];
    // html2canvas(element)
    //   .then((canvas: any) => {
    //     doc.addImage(canvas.toDataURL("image/jpeg"), "JPEG", 0, 10);
    //     doc.save("save-two");
    //   })

  }

}
