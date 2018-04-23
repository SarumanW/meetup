import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import {Evento} from "../events/event";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class FolderService {

  constructor(private http: HttpClient) { }

  getEvents(folderId : number) :  Observable<Evento[]>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    console.log("get events service")

    return this.http
      .get<any>('api/events/folder/' + folderId, {headers: headers});
  }
}
