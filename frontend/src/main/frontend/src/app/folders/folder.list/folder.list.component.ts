import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Folder} from "../folder";
import {FolderListService} from "../folder.list.service";

@Component({
  selector: 'app-folder',
  templateUrl: './folder.list.component.html',
  styleUrls: ['./folder.list.component.css']
})
export class FolderListComponent implements OnInit {

  folders: Folder[];
  selectedFolder: Folder;

  constructor(private folderService: FolderListService,
              private router : Router) {
    this.selectedFolder = new Folder;
  }

  ngOnInit() {
    this.getFolders();
  }

  getFolders(): void {
    this.folderService.getFolders()
      .subscribe(folders => this.folders = folders)
  }

  openEvents(folder : Folder): void {
    this.router.navigate(['/profile/details/login/folder', folder.id])
  }

}
