import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {EmptyObservable} from "rxjs/observable/EmptyObservable";

@Injectable()
export class FriendService {

  constructor(private http: HttpClient) {
  }

  getFriends(login:string): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get<any>(`api/profile/${login}/friends`, {headers:headers});
  }

  getUsersByUsernamePart(userName: string, type: string = 'unknown'): Observable<any> {
    let currentUser = JSON.parse(localStorage.currentUser);

    if (userName.trim().length === 0) {
      return new EmptyObservable();
    }

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${currentUser.token}`);

    return this.http.get<any>(`api/profile/${currentUser.id}/search`,{headers:headers, params: {'username' : userName, 'type': type}});
  }

  getFriendsRequests(): Observable<any> {
    let currentUser = JSON.parse(localStorage.currentUser);

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${currentUser.token}`);
    return this.http.get<any>(`api/profile/${currentUser.id}/friends/requests`, {headers:headers});
  }

  addFriend(newFriend: string): Observable<any> {
    let currentUser = JSON.parse(localStorage.currentUser);

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${currentUser.token}`);

    return this.http.post<any>(`api/profile/${currentUser.id}/friends`, newFriend, {headers: headers});
  }

  confirmFriend(confirmedFriend: number): Observable<any> {
    let currentUser = JSON.parse(localStorage.currentUser);

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${currentUser.token}`);

    return this.http.post<any>(`api/profile/${currentUser.id}/friends/confirm`, confirmedFriend, {headers: headers});
  }

  deleteFriend(id: number): Observable<any> {
    let currentUser = JSON.parse(localStorage.currentUser);

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${currentUser.token}`);

    return this.http.delete<any>(`api/profile/${currentUser.id}/friends/${id}`,  {headers:headers});
  }

  getRelation(otherUserId: number): Observable<any> {
    let currentUser = JSON.parse(localStorage.currentUser);

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${currentUser.token}`);

    return this.http.get(`api/profile/${currentUser.id}/relations/${otherUserId}`, {headers: headers});
  }
}
