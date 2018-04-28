import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Evento} from "../event";
import {Profile} from "../../account/profile";
import {NgxSpinnerService} from "ngx-spinner";
import {FolderService} from "../../folders/folder.service";
import {EventService} from "../event.service";


@Component({
  selector: 'app-event-list',
  templateUrl: './event.list.component.html',
  styleUrls: ['./event.list.component.css'],
})
export class EventListComponent implements OnInit {
  events: Evento[] = [];
  folderId: number;
  eventType: string;
  state: string = "folders";
  profile: Profile;

  page: number = 1;
  itemsPerPage: number = 10;
  maxSize: number = 5;
  numPages: number = 1;
  length: number = 0;

  rows: Array<any> = [];
  columns: Array<any> = [
    {title: 'EventId', name: 'eventId', sort: 'asc'},
    {title: 'Name', name: 'name', filtering: {filterString: '', placeholder: 'Filter by name'}},
    {title: 'Date', name: 'eventDate',
      filtering: {filterString: '', placeholder: 'Filter by date'}},
    {title: 'Description', name: 'description', filtering: {filterString: '', placeholder: 'Filter by description'}},
    {title: 'Place', name: 'place', filtering: {filterString: '', placeholder: 'Filter by place'}},
    {title: 'Periodicity', name: 'periodicity', filtering: {filterString: '', placeholder: 'Filter by periodicity'}},
  ];

  config: any = {
    paging: true,
    sorting: {columns: this.columns},
    filtering: {filterString: ''},
    className: ['table-striped', 'table-bordered']
  };

  constructor(private folderService: FolderService,
              private eventService: EventService,
              private route: ActivatedRoute,
              private router: Router,
              private spinner: NgxSpinnerService) {

  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      this.folderId = params['folderId'];
      this.eventType = params['type'];
    });

    this.profile = JSON.parse(localStorage.getItem('currentUser'));

    this.getEventsByType();

    this.onChangeTable(this.config);
  }

  // getEvents() {
  //   this.spinner.show();
  //
  //   this.folderService.getEvents(this.folderId)
  //     .subscribe(events => {
  //
  //       this.events = events;
  //       this.length = this.events.length;
  //       this.onChangeTable(this.config);
  //
  //       this.spinner.hide();
  //     })
  // }

  getEventsByType() {
    this.spinner.show();
    let type: string;

    switch(this.eventType){
      case 'public':{
        type = 'EVENT';
        break;
      }
      case 'private':{
        type = 'EVENT';
        break;
      }
      case 'draft':{
        type = 'EVENT';
        break;
      }
      case 'note':{
        type = 'NOTE';
        break;
      }
    }

    this.eventService.getEventsByType(type, this.folderId)
      .subscribe(events => {

        this.events = events;
        this.length = this.events.length;
        this.onChangeTable(this.config);

        this.spinner.hide();
      })
  }

  openEvent(event: Evento) {
    this.router.navigate(["/" + this.profile.login + "/folders/" +
    event.folderId + "/" + this.eventType + "/" + event.eventId])
  }

  public changePage(page: any, data: Array<any> = this.events): Array<any> {
    let start = (page.page - 1) * page.itemsPerPage;
    let end = page.itemsPerPage > -1 ? (start + page.itemsPerPage) : data.length;
    return data.slice(start, end);
  }

  public changeSort(data: any, config: any): any {
    if (!config.sorting) {
      return data;
    }

    let columns = this.config.sorting.columns || [];
    let columnName: string = void 0;
    let sort: string = void 0;

    for (let i = 0; i < columns.length; i++) {
      if (columns[i].sort !== '' && columns[i].sort !== false) {
        columnName = columns[i].name;
        sort = columns[i].sort;
      }
    }

    if (!columnName) {
      return data;
    }

    return data.sort((previous: any, current: any) => {
      if (previous[columnName] > current[columnName]) {
        return sort === 'desc' ? -1 : 1;
      } else if (previous[columnName] < current[columnName]) {
        return sort === 'asc' ? -1 : 1;
      }
      return 0;
    });
  }

  public changeFilter(data: any, config: any): any {
    let filteredData: Array<any> = data;

    this.columns.forEach((column: any) => {
      if (column.filtering) {
        filteredData = filteredData.filter((item: any) => {
            return item[column.name].match(column.filtering.filterString);
        });
      }
    });

    if (!config.filtering) {
      return filteredData;
    }

    if (config.filtering.columnName) {
      return filteredData.filter((item: any) =>
        item[config.filtering.columnName].match(this.config.filtering.filterString));
    }

    let tempArray: Array<any> = [];
    filteredData.forEach((item: any) => {
      let flag = false;

        this.columns.forEach((column: any) => {
          if (item[column.name].toString().match(this.config.filtering.filterString)) {
            flag = true;
          }
        });


      if (flag) {
        tempArray.push(item);
      }
    });
    filteredData = tempArray;

    return filteredData;
  }

  public onChangeTable(config: any, page: any = {page: this.page, itemsPerPage: this.itemsPerPage}): any {
    if (config.filtering) {
      Object.assign(this.config.filtering, config.filtering);
    }

    if (config.sorting) {
      Object.assign(this.config.sorting, config.sorting);
    }

    let filteredData = this.changeFilter(this.events, this.config);
    let sortedData = this.changeSort(filteredData, this.config);
    this.rows = page && config.paging ? this.changePage(page, sortedData) : sortedData;
    this.length = sortedData.length;
  }

  public onCellClick(data: any): any {
    console.log(data.row);
    this.openEvent(data.row);
  }

}
