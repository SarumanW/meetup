import {Injectable} from "@angular/core";
import {Observable} from 'rxjs/Observable';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Item} from "./item";

@Injectable()
export class WishService {

  currentUser;
  prePath: string;

  constructor(private http: HttpClient) {
  }

  initPrePath() {
    this.currentUser = JSON.parse(localStorage.currentUser);
    this.prePath = `api/users/${this.currentUser.id}`;
  }

  getWishItem(itemId: number, login: string): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${itemId}/login/`+login;

    return this.http.get<any>(url, {headers: headers});
  }

  addWishItem(item: Item): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/`;

    return this.http.post(url, item, {headers: headers});
  }

  addExistWishItem(item: Item): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${item.itemId}`;

    return this.http.post(url, item, {headers: headers});
  }

  editWishItem(item: Item): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${item.itemId}`;

    return this.http.put(url, item, {headers: headers});
  }

  deleteWishItem(item: Item): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${item.itemId}`;

    return this.http.delete(url, {headers: headers});
  }

  bookWishItem(item: Item): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${item.itemId}/owner/${item.ownerId}`;

    return this.http.post(url, item, {headers: headers});
  }

  unbookWishItem(item: Item): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${item.itemId}/owner/${item.ownerId}`;

    return this.http.delete(url, {headers: headers});
  }

  addLike(itemId: number) {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${itemId}/like`;

    return this.http.post(url, {}, {headers: headers})
  }

  removeLike(itemId: number) {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${itemId}/like`;

    return this.http.delete(url, {headers: headers})
  }

  getLoginsWhoLiked(itemId: number) {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    let url = `${this.prePath}/items/${itemId}/likes`;

    return this.http.get(url, {headers: headers})
  }
}
