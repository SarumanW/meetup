import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Item} from "./item";
import {WishListComponent} from "./wish.list/wish.list.component";

@Injectable()
export class WishListService {

  constructor(private http: HttpClient) {}

  getWishList(category: string, login = '', tags: string[] = []): Observable<Item[]> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    let params = new HttpParams();
    if (tags.length !== 0) {
      tags.forEach(function (tag) {
        params.set("tag", tag);
      });
    }

    switch (category) {
      case WishListComponent.BOOKINGS_CATEGORY: {
        return this.http
          .get<any>('api/wishes/bookings/', {headers: headers, params: params});
      }
      case WishListComponent.RECOMMENDATIONS_CATEGORY: {
        return this.http
          .get<any>('api/wishes/recommendations/', {headers: headers, params: params});
      }
      default: {
        return this.http
          .get<any>('api/wishes/' + login, {headers: headers, params: params});
      }
    }
  }

}
