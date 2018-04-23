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
  state:string="folders";

  constructor(private folderListService: FolderListService,
              private router : Router) {
    this.selectedFolder = new Folder;
  }

  ngOnInit() {
    this.getFoldersList()
  }

  getFoldersList():void{
    this.folderListService.getFoldersList().
      subscribe(
        folders => {
          this.folders = folders
        })
  }

  addFolder(folderName){
    let folder : Folder = new Folder();
    folder.name = folderName.value;
    folder.userId = JSON.parse(localStorage.currentUser).id;

    console.log(folder);

    this.folderListService.addFolder(folder).subscribe();
    this.getFoldersList();
  }

}
