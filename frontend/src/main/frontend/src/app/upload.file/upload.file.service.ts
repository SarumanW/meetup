import {Injectable} from '@angular/core';
import {HttpClient, HttpRequest, HttpEvent, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Item} from "../wishes/item";

@Injectable()
export class UploadFileService {

  constructor(private http: HttpClient) {
  }

  pushProfileFileToStorage(file: File): Observable<HttpEvent<{}>> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);
    let formdata: FormData = new FormData();

    formdata.append('file', file);

    const req = new HttpRequest('POST', '/api/profile/upload', formdata, {
      reportProgress: true,
      responseType: 'text',
      headers: headers
    });
    return this.http.request(req);
  }

  pushWishFileToStorage(file: File): Observable<HttpEvent<{}>> {
    let currentUser = JSON.parse(localStorage.currentUser);
    let prePath = `api/users/${currentUser.id}`;

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${currentUser.token}`);
    let formdata: FormData = new FormData();

    formdata.append('file', file);

    const req = new HttpRequest('POST', `${prePath}/items/upload`, formdata, {
      reportProgress: true,
      responseType: 'text',
      headers: headers
    });
    return this.http.request(req);
  }
}
