import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Folder} from "../folder";
import {FolderListService} from "../folder.list.service";
import {FolderService} from "../folder.service";
import {Observable} from "rxjs/Observable";
import {ToastrService} from "ngx-toastr";
import {Profile} from "../../account/profile";
import {NgxSpinnerService} from "ngx-spinner";

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

  constructor(private folderListService: FolderListService,
              private spinner: NgxSpinnerService,
              private toastr: ToastrService) {

    this.selectedFolder = new Folder;
  }

  ngOnInit() {
    this.getFoldersList();
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
  }

  getFoldersList(): void {
    this.spinner.show();

    this.folderListService.getFoldersList().subscribe(
      folders => {
        this.folders = folders
        this.spinner.hide();
      })
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

  showSuccess() {
    this.toastr.info('Your events were moved to general folder', 'Attention!', {
      timeOut: 3000,
      positionClass: 'toast-bottom-left',
      closeButton: true
    });
  }

}
