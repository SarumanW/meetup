import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import { CalendarEvent } from 'angular-calendar';
import {startOfDay, addDays} from 'date-fns';
import {isSameDay, isSameMonth} from "ngx-bootstrap/chronos/utils/date-getters";
import {CalendarService} from '../calendar.service';
import {Evento} from "../../events/event";
import {colors} from "../calendar.utils/colors";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  // changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  state: string = "calendar";
  view: string = 'month';
  viewDate: Date = new Date();
  realEvents: Evento[];
  events: CalendarEvent[] = [];

  activeDayIsOpen: boolean = true;

  constructor(private calendarService: CalendarService,
              private spinner: NgxSpinnerService,
              private router: Router) {
  }

  ngOnInit() {
    this.getRealEvents();
  }

  filterEvents(){
    for(let realEvent of this.realEvents){
      let calendarEvent : any;
      calendarEvent = {};

      calendarEvent.id = realEvent.eventId;
      calendarEvent.title = realEvent.name;
      calendarEvent.start = new Date(realEvent.eventDate);

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
      })
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

    this.router.navigate(["/" + login + "/folders/18/public/" + event.id])
  }

}
