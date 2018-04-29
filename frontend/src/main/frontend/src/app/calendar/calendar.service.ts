import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class CalendarService {

  constructor(private http: HttpClient) {
  }

  getUserEvents(userId: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get<any>('api/events/' + userId, {headers: headers});

  }

}
