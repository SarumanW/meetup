import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import {Profile} from "./profile";

@Injectable()
export class AccountService {

  constructor(private http: HttpClient) {}

  save(account: any): Observable<any> {
    return this.http.post('api/register', account);
  }

  upImg(img:any):Observable<any>{
    return this.http.post('api/profile/img', img);
  }

  login(account: any): Observable<Profile> {

    return this.http.post<any>('api/login', account)
      .map(user => {
        if(user && user.token){
          localStorage.setItem('currentUser', JSON.stringify(user));
        }
        return user;
      })
  }

  profile(login: string):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get('api/profile/' + login, {headers: headers});
  }

  update(account:any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.put('/api/profile/update', account, {headers: headers});
  }

  recovery(data: any):Observable<any>{
    return this.http.post('api/recovery/',data);
  }
}
