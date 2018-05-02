import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  NgbDatepickerModule,
  NgbTimepickerModule
} from '@ng-bootstrap/ng-bootstrap';
import { CalendarModule } from 'angular-calendar';
import { DateTimePickerComponent } from './date-time-picker.component';
import {CalendarHeaderComponent} from "./calendar.header.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbDatepickerModule.forRoot(),
    NgbTimepickerModule.forRoot(),
    CalendarModule
  ],
  declarations: [CalendarHeaderComponent, DateTimePickerComponent],
  exports: [CalendarHeaderComponent, DateTimePickerComponent]
})
export class UtilsModule {}
