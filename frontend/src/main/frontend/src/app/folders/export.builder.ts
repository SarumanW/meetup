import {Evento} from "../events/event";

export class Builder {
  columns: any[] = [];
  rows: any[] = [];
  events: Evento[] = [];
  checkboxes: any[] = [];

  constructor(private periodEvents: Evento[],
              private checkBoxes: any[]) {
    this.events = periodEvents;
    this.checkboxes = this.checkBoxes;
  }

  render() {
    this.columns = this.checkboxes
      .filter(obj => obj.checked)
      .map(obj => obj.columnName);

    this.periodEvents.forEach(eventObj => {
      let forceEvent: any[] = [];

      this.checkboxes.forEach(checkboxObj => {
        if (checkboxObj.checked) {
          forceEvent.push(eventObj[checkboxObj.objectProperty]);
        }
      });

      this.rows.push(forceEvent);
    });

    return this;
  }

  buildColumns() {
    return this.columns;
  }

  buildRows(){
    return this.rows;
  }
}
