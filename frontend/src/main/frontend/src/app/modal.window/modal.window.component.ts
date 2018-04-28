import {Component, OnInit} from "@angular/core";

@Component({
  selector: 'app-modal-window',
  templateUrl: './modal.window.component.html',
  styleUrls: [ './modal.window.component.css' ]
})
export class ModalWindow implements OnInit {

  public visible = false;
  public visibleAnimate = false;

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
