import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import {LoginAccount} from "./login.account";
import 'rxjs/add/operator/map'

@Injectable()
export class AccountService {

  constructor(private http: HttpClient) {}

  save(account: any): Observable<any> {
    var out : Observable<any> = this.http.post('api/register', account);
    out.subscribe(message => console.log(message));
    return out;
  }

  login(account: any): Observable<any> {
    console.log(localStorage.getItem("currentUser"))
    return this.http.post<any>('api/login', account)
      .map(user => {
        // login successful if there's a jwt token in the response\
        console.log("accout service working")
        console.log(user.token);
        if(user && user.token){
          localStorage.setItem('currentUser', JSON.stringify(user));
        }
        return user;
      });
  }
}
