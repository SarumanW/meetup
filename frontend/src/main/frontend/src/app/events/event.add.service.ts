import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class EventAddService {

  constructor(private http: HttpClient) { }

  addEvent(event: any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    console.log('wow');
    return this.http.post('api/events/add', event, {headers: headers});
  }

}
