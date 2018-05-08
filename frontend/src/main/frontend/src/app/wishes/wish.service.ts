import {Injectable} from "@angular/core";
import { Observable } from 'rxjs/Observable';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class WishService {

  constructor(private http: HttpClient) {}

  getWishItem(id : string):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get<any>("api/item/"+id,{headers: headers});
  }

}
