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
    console.log(account);
    return this.http.post('api/register', account);
  }

  upImg(img:any):Observable<any>{
    console.log("assad");
    return this.http.post('api/profile/img', img);
  }

  login(account: any): Observable<Profile> {
    console.log("login " + account.login);
    console.log("password " + account.password);

    return this.http.post<any>('api/login', account)
      .map(user => {
        console.log("account service working")
        console.log("token" + user.token);
        if(user && user.token){
          localStorage.setItem('currentUser', JSON.stringify(user));
        }
        return user;
      })
  }

  profile(account: any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get<any>('api/profile/' + account.id, {headers: headers});
  }

  update(account:any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    console.log("updating");
    return this.http.post<any>('/api/profile/update', account, {headers: headers});
  }

  recovery(data: any):Observable<any>{
    return this.http.post('api/recovery/',data);
  }
}
