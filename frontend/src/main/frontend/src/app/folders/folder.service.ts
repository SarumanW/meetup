import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import {Evento} from "../events/event";
import {EVENTS} from "../events/mock-events";

@Injectable()
export class FolderService {

  constructor() { }

  getEvents(id : number) :  Observable<Evento[]>{
    let events : Evento[];

    events = EVENTS.filter(event => event.folderId === id);

    return of(events);

  }
}
