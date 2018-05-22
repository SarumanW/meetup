import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class CalendarService {

  currentUser;
  prePath: string;

  constructor(private http: HttpClient) {
  }

  initPrePath() {
    this.currentUser = JSON.parse(localStorage.currentUser);
    this.prePath = `api/users/${this.currentUser.id}`;
  }

  getUserEvents(userId: number): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http
      .get<any>(`${this.prePath}/events`, {headers: headers});

  }

}
