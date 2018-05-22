import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Item} from "./item";
import {WishListComponent} from "./wish.list/wish.list.component";

@Injectable()
export class WishListService {

  currentUser;
  prePath: string;

  constructor(private http: HttpClient) {}

  initPrePath() {
    this.currentUser = JSON.parse(localStorage.currentUser);
    this.prePath = `api/users/${this.currentUser.id}`;
  }

  getWishList(category: string, login = '', tags: string[] = []): Observable<Item[]> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let params = new HttpParams();

    switch (category) {
      case WishListComponent.BOOKINGS_CATEGORY: {
        return this.http
          .get<any>(`${this.prePath}/wishes/bookings/`, {headers: headers, params: params});
      }
      case WishListComponent.RECOMMENDATIONS_CATEGORY: {
        return this.http
          .post<any>(`${this.prePath}/wishes/recommendations/`, tags, {headers: headers, params: params});
      }
      default: {
        return this.http
          .get<any>(`${this.prePath}/wishes/` + login, {headers: headers, params: params});
      }
    }
  }

  getQueryTagList(queryTagPart: string): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get<any>(`${this.prePath}/wishes/tags/${queryTagPart}`, {headers: headers});
  }
}
