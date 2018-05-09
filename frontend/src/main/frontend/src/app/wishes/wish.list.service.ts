import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Item} from "./item";
import {WishListComponent} from "./wish.list/wish.list.component";

@Injectable()
export class WishListService {
  readonly GET_OWN_WISH_LIST = 'api/wishes/';
  readonly GET_RECOMMENDATIONS_WISH_LIST = 'api/wishes/recommendations/';
  readonly GET_BOOKINGS_WISH_LIST = 'api/wishes/bookings/';
  readonly POST_ADD_ITEM = 'api/item/';
  readonly POST_BOOK_WISH_ITEM = 'api/item/';
  readonly POST_UNBOOK_WISH_ITEM = 'api/item/';

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
          .get<any>(this.GET_BOOKINGS_WISH_LIST, {headers: headers, params: params});
      }
      case WishListComponent.RECOMMENDATIONS_CATEGORY: {
        return this.http
          .get<any>(this.GET_RECOMMENDATIONS_WISH_LIST, {headers: headers, params: params});
      }
      default: {
        return this.http
          .get<any>(this.GET_OWN_WISH_LIST + login, {headers: headers, params: params});
      }
    }
  }

  addWishItem(item: Item): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.post(this.POST_ADD_ITEM, item, {headers: headers});
  }

  deleteWishItem(item: Item): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.delete(`api/item/${item.itemId}/delete`, {headers: headers});
  }


  //todo create booking
  bookWishItem(item: Item): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    let url = `/api/item/${item.itemId}/owner/${item.ownerId}/booker/${item.bookerId}`;

    return this.http.post(url, item, {headers: headers});
  }

  //todo create unbooking
  unbookWishItem(item: Item): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    let url = `/api/item/${item.itemId}/owner/${item.ownerId}/booker/${item.bookerId}`;

    return this.http.delete(url, {headers: headers});
  }

}
