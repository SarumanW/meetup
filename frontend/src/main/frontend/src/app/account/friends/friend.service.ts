import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";

@Injectable()
export class FriendService {

  constructor(private http: HttpClient) {
  }

  getFriends(): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.get<any>('api/profile/friends', {headers:headers})
      .map(friends => {
        return friends;
      })
  }

  getFriendsRequests(): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.get<any>('api/profile/friendsRequests', {headers:headers})
      .map(requests => {
        return requests;
      })
  }

  addFriend(newFriend: String): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.post<any>('api/profile/addFriend', newFriend, {headers:headers}).map(message => {
      console.log(message);
      return message;
    });
  }

  confirmFriend(confirmedFriend: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.post<any>('api/profile/confirmFriend', confirmedFriend, {headers:headers}).map(friend => {
      return friend;
    });
  }

  deleteFriend(id: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    return this.http.post<any>('api/profile/deleteFriend', id, {headers:headers}).map(id => {
      return id;
    });
  }
}
