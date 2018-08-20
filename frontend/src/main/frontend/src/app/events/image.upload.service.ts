import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class ImageUploadService {

  constructor(private http: HttpClient) { }

  pushFileToStorage(file: File, userId: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    let formdata: FormData = new FormData();

    formdata.append('file', file);

    return this.http.post('/api/users/' + userId + '/events/upload', formdata, {headers: headers});
  }

}
