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

  addFriend(newFriend: String): Observable<any> {
    return this.http.post<any>('api/profile/addFriend', newFriend).map(friend => {
      return friend;
    });
  }

  deleteFriend(id: number): Observable<any> {
    return this.http.post<any>('api/profile/deleteFriend', id).map(id => {
      return id;
    });
  }
}
