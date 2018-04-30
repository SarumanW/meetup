import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/observable/throw';

@Injectable()
export class FriendService {

  constructor(private http: HttpClient) {
  }

  getFriends(login: string): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.get<any>('api/profile/' + login + '/friends', {headers: headers})
      .map(friends => {
        return friends;
      })
  }

  getFriendsRequests(): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.get<any>('api/profile/friendsRequests', {headers: headers})
      .map(requests => {
        return requests;
      })
  }

  addFriend(newFriend: String): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.post<any>('api/profile/addFriend', newFriend, {headers: headers});
  }

  confirmFriend(confirmedFriend: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.post<any>('api/profile/confirmFriend', confirmedFriend, {headers: headers});
  }

  deleteFriend(id: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.post<any>('api/profile/deleteFriend', id, {headers: headers});
  }
}
