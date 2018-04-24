import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";

@Injectable()
export class FriendService {

  constructor(private http: HttpClient) {
  }

  getFriends(): Observable<any> {
    return this.http.get<any>('api/profile/friends')
      .map(friends => {
        return friends;
      })
  }

  getFriendsRequests(): Observable<any> {
    return this.http.get<any>('api/profile/friendsRequests')
      .map(requests => {
        return requests;
      })
  }

  addFriend(newFriend: String): Observable<any> {
    return this.http.post<any>('api/profile/addFriend', newFriend).map(message => {
      console.log(message);
      return message;
    });
  }

  confirmFriend(confirmedFriend: number): Observable<any> {
    return this.http.post<any>('api/profile/confirmFriend', confirmedFriend).map(friend => {
      return friend;
    });
  }

  deleteFriend(id: number): Observable<any> {
    return this.http.post<any>('api/profile/deleteFriend', id).map(id => {
      return id;
    });
  }
}
