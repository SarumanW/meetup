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

  constructor(private http: HttpClient) {}

  getWishList(category: string, login?: string): Observable<Item[]> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    // let params = new HttpParams();
    // params.set("category", category);

    switch (category) {
      case WishListComponent.BOOKINGS_CATEGORY: {
        return this.http
          .get<any>(this.GET_BOOKINGS_WISH_LIST + login, {headers: headers});
      }
      case WishListComponent.RECOMMENDATIONS_CATEGORY: {
        return this.http
          .get<any>(this.GET_RECOMMENDATIONS_WISH_LIST, {headers: headers});
      }
      default: {
        return this.http
          .get<any>(this.GET_OWN_WISH_LIST + login, {headers: headers});
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

    return this.http.delete(`api/wishes/${item.itemId}`, {headers: headers});
  }
}
