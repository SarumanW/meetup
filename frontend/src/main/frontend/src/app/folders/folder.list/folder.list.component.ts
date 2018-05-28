import {Component, OnInit} from "@angular/core";
import {Folder} from "../folder";
import {FolderListService} from "../folder.list.service";
import {ToastrService} from "ngx-toastr";
import {Profile} from "../../account/profile";
import {NgxSpinnerService} from "ngx-spinner";
import {Evento} from "../../events/event";
import {EventService} from "../../events/event.service";
import 'jspdf-autotable';
import {Builder} from "../export.builder";
import {AppComponent} from "../../app.component";

declare let jsPDF;

@Component({
  selector: 'app-folders',
  templateUrl: './folder.list.component.html',
  styleUrls: ['./folder.list.component.css']
})
export class FolderListComponent implements OnInit {

  folders: Folder[];
  selectedFolder: Folder;
  state: string = "folders";
  nameInput: string = "";
  profile: Profile;
  isSent: boolean;

  currentDate: string;
  startDate: string;
  endDate: string;

  checkboxes: any[] = [
    {
      checked: true,
      columnName: "Name",
      objectProperty: "name",
    },
    {
      checked: false,
      columnName: "Date",
      objectProperty: "eventDate",
    },
    {
      checked: false,
      columnName: "Description",
      objectProperty: "description",
    },
    {
      checked: false,
      columnName: "Periodicity",
      objectProperty: "periodicity",
    }
  ];

  constructor(private folderListService: FolderListService,
              private spinner: NgxSpinnerService,
              private toastr: ToastrService,
              private eventService: EventService,
              private appComponent: AppComponent) {

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
        this.folders = folders;
        this.spinner.hide();
      },
      error => this.appComponent.showError(error, "Error"))
  }

  getPeriodEvents(start: string, end: string): void {
    this.spinner.show();

    this.eventService.getEventsInPeriod(start, end).subscribe(
      events => {
        let doc = new jsPDF('p', 'pt');
        let docName = "events-plan-" + this.currentDate + ".pdf";

        let builder: Builder = new Builder(events, this.checkboxes);

        let columns = builder.render().buildColumns();
        let rows = builder.buildRows();

        let s = this.startDate.split(' ')[0];
        let e = this.endDate.split(' ')[0];

        doc.autoTable(columns, rows, {
          addPageContent: function () {
            doc.text("Your events on period: " + s + " to " + e, 40, 30);
          }
        });

        this.spinner.hide();

        if (this.isSent) {
          let data = new File([doc.output()], docName);

          let formData = new FormData();
          formData.append('file', data);

          this.eventService.uploadEventsPlan(formData).subscribe()
        }

        doc.save(docName);
      },
      error => this.appComponent.showError(error, "Error")
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
        },
        error => this.appComponent.showError(error, "Error"));

    this.nameInput = null;
  }

  deleteFolder(folder) {

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
          error => {
            this.appComponent.showError(error, "Error");
            this.spinner.hide();
          });
  }

  showSuccess() {
    this.toastr.info('Your events were moved to general folder', 'Attention!', {
      timeOut: 3000,
      positionClass: 'toast-top-right',
      closeButton: true
    });
  }

  downloadPlan() {
    this.formatDate();
    this.getPeriodEvents(this.startDate, this.endDate);
  }

}
