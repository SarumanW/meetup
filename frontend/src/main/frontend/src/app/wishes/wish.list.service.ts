import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Item} from "./item";

@Injectable()
export class WishListService {

  constructor(private http: HttpClient) {}

  getWishList(): Observable<Item[]> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get<any>('api/wishes/', {headers: headers});
  }

  addWishItem(item: any): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.post('api/wishes/add', item, {headers: headers});
  }
}
