import { Injectable } from '@angular/core';
import {HttpClient, HttpHandler, HttpRequest, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import {Headers, RequestOptions} from '@angular/http';
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import {Router} from "@angular/router";
import {MessageService} from "./message.service";

@Injectable()
export class AccountService {

  constructor(private http: HttpClient,
              public messageService : MessageService) {}

  save(account: any): Observable<any> {
    var out : Observable<any> = this.http.post('api/register', account);
    out.subscribe(message => console.log(message));
    return out;
  }

  login(account: any): Observable<any> {
    console.log("acc service - user -" + localStorage.getItem("currentUser"))
    console.log("login " + account.login);
    console.log("password " + account.password);

    return this.http.post<any>('api/login', account)
      .map(user => {
        // login successful if there's a jwt token in the response
        console.log("account service working")
        console.log("token" + user.token);
        if(user && user.token){
          localStorage.setItem('currentUser', JSON.stringify(user));
        }
        return user;
      }).catch(
        (err: any) : any => {
          console.log("check section");
          if(err.status === 500){
            console.log(err.status)
            this.messageService.message = err.message;
          }
          if(err.status === 400){
            console.log(err.status)
          }
        }
      )
  }

  profile(account: any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get<any>('api/profile/details/' + account.login, {headers: headers});
  }
}
