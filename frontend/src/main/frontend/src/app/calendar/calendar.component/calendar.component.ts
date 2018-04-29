import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NgxSpinnerService} from "ngx-spinner";
import {EventService} from "../../events/event.service";
import { CalendarEvent } from 'angular-calendar';
import {startOfDay, addDays} from 'date-fns';
import {isSameDay, isSameMonth} from "ngx-bootstrap/chronos/utils/date-getters";
import {CalendarService} from '../calendar.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  state: string = "calendar";
  view: string = 'month';
  viewDate: Date = new Date();
  events: CalendarEvent[] = [
    {
      id : 1,
      title: "First event",
      start: new Date(),
    },
    {
      title: "Second event",
      start: new Date(),
    },
    {
      title: "Third event",
      start: addDays(new Date(), 1),
    },
    {
      title: "Fourth event",
      start: addDays(new Date(), 1),
    }];

  activeDayIsOpen: boolean = true;

  constructor(private calendarService: CalendarService,
              private spinner: NgxSpinnerService,
              private router: Router) {
  }

  ngOnInit() {

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
