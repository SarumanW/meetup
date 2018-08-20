import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import { CalendarEvent } from 'angular-calendar';
import {startOfDay, addDays} from 'date-fns';
import {isSameDay, isSameMonth} from "ngx-bootstrap/chronos/utils/date-getters";
import {CalendarService} from '../calendar.service';
import {Evento} from "../../events/event";
import {colors} from "../calendar.utils/colors";
import {AppComponent} from '../../app.component';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  state: string = "calendar";
  view: string = 'month';
  viewDate: Date = new Date();
  realEvents: Evento[];
  events: CalendarEvent[] = [];
  eventsFolders: any = new Map();

  activeDayIsOpen: boolean = true;

  constructor(private calendarService: CalendarService,
              private spinner: NgxSpinnerService,
              private router: Router,
              private appComponent: AppComponent) {
  }

  ngOnInit() {
    this.getRealEvents();
  }

  filterEvents(){
    for(let realEvent of this.realEvents){
      let calendarEvent : any = {};

      calendarEvent.id = realEvent.eventId;
      calendarEvent.title = realEvent.name;
      calendarEvent.start = new Date(realEvent.eventDate);

      this.eventsFolders.set(realEvent.eventId, realEvent.folderId);

      switch(realEvent.eventTypeId){
        case 1:
          calendarEvent.color = colors.blue;
          break;
        case 3:
          calendarEvent.color = colors.red;
      }

      if(realEvent.isDraft){
        calendarEvent.color = colors.yellow;
      }

      this.events.push(calendarEvent);
    }
  }

  getRealEvents(){
    this.spinner.show();
    let id = JSON.parse(localStorage.currentUser).id;

    this.calendarService.getUserEvents(id)
      .subscribe((events) => {
        this.realEvents = events;
        this.filterEvents();
        this.spinner.hide();
      },error => {
          this.spinner.hide();
          this.appComponent.showError(error, 'Error');
        }
      )
  }

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
        this.viewDate = date;
      }
    }
  }

  handleEvent(event: CalendarEvent): void {
    let login = JSON.parse(localStorage.currentUser).login;
    let folderId = this.eventsFolders.get(event.id);

    this.router.navigate(["/" + login + "/folders/" + folderId + "/public/" + event.id ])
  }

}
