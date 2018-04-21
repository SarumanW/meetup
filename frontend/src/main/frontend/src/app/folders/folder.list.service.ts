import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import {Folder} from "./folder";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class FolderListService {

  constructor(private http: HttpClient) { }

  getFoldersList(userId: any):Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get<any>('api/profile/' + userId + '/folders', {headers: headers});
  }
}
