import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AccountService {

  constructor(private http: HttpClient) {}

  save(account: any): Observable<any> {
    return this.http.post('api/register', account);
  }

  login(account: any): Observable<any> {
    return this.http.post('api/login', account);
  }
}
