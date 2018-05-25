import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import {Profile} from "./profile";

@Injectable()
export class AccountService {

  constructor(private http: HttpClient) {}

  //registration

  save(account: any): Observable<any> {
    return this.http.post('auth/register', account);
  }

  confirm(data: any):Observable<any>{
    return this.http.post('auth/confirmation/',data);
  }

  upImg(img:any):Observable<any>{
    return this.http.post('api/profile/img', img);
  }

  //login

  login(account: any): Observable<Profile> {

    return this.http.post<any>('auth/login', account)
      .map(user => {
        if (user && user.token) {
          localStorage.setItem('currentUser', JSON.stringify(user));
        }
        return user;
      })
  }

  //recovery

  sendRecovery(email: string): Observable<any>{
    return this.http.get('auth/recovery/' + email);
  }

  recovery(data: any):Observable<any>{
    return this.http.post('auth/recovery/', data);
  }

  profile(login: string):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get('api/profile/' + login, {headers: headers});
  }

  profileWithEvent(login: string):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get(`api/profile/${login}/event/pined`, {headers: headers});
  }

  update(account:any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.put('api/profile/', account, {headers: headers});
  }

  checkPassword(account:any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.post('api/check/password/', account, {headers: headers});
  }

  changePassword(account:any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.post('api/change/password/', account, {headers: headers});
  }

  getLoginById(id:any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get('/api/profile/login/'+id, {headers: headers, responseType: 'text'} );
  }

}
