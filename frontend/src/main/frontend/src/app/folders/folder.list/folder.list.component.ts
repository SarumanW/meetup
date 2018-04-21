import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Folder} from "../folder";
import {FolderListService} from "../folder.list.service";
import {FolderService} from "../folder.service";

@Component({
  selector: 'app-folder',
  templateUrl: './folder.list.component.html',
  styleUrls: ['./folder.list.component.css']
})
export class FolderListComponent implements OnInit {

  folders: Folder[];
  selectedFolder: Folder;

  constructor(private folderListService: FolderListService,
              private router : Router) {
    this.selectedFolder = new Folder;
  }

  ngOnInit() {
    this.getFoldersList()
  }

  getFoldersList():void{
    this.folderListService.getFoldersList(JSON.parse(localStorage.getItem("currentUser")).id).
      subscribe(
        folders => {
          this.folders = folders
        })
  }

}
