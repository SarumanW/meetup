import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Item} from "./item";
import {Profile} from "../account/profile";

@Injectable()
export class WishListService {

  constructor(private http: HttpClient) {}

  getWishList(category: string): Observable<Item[]> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    let params = new HttpParams();
    params.set("category", category);

    return this.http
      .get<any>('api/wishes/', {headers: headers, params: params});
  }

  addWishItem(item: Item): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.post('api/item', item, {headers: headers});
  }

  deleteWishItem(item: Item): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.delete(`api/wishes/${item.itemId}`, {headers: headers});
  }
}
