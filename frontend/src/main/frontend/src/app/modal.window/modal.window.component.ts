import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Folder} from "../folders/folder";
import {FolderService} from "../folders/folder.service";
import {Evento} from "../events/event";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-modal-window',
  templateUrl: './modal.window.component.html',
  styleUrls: [ './modal.window.component.css' ]
})
export class ModalWindow implements OnInit {

  public visible = false;
  private visibleAnimate = false;

  constructor() {}

  ngOnInit() {
  }

  public show(): void {
    this.visible = true;
    setTimeout(() => this.visibleAnimate = true, 100);
  }

  public hide(): void {
    this.visibleAnimate = false;
    setTimeout(() => this.visible = false, 300);
  }
}
