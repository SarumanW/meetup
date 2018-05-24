import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class ImageUploadService {

  constructor(private http: HttpClient) { }

  pushFileToStorage(file: File): Observable<any> {
    let user = JSON.parse(localStorage.currentUser);

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${user.token}`);

    let formdata: FormData = new FormData();

    formdata.append('file', file);

    return this.http.post(`/api/users/${user.id}/events/upload`, formdata, {headers: headers});
  }

}
