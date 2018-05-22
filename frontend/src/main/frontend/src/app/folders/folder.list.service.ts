import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class FolderListService {

  currentUser;
  prePath: string;

  constructor(private http: HttpClient) {
  }

  initPrePath() {
    this.currentUser = JSON.parse(localStorage.currentUser);
    this.prePath = `api/users/${this.currentUser.id}`;
  }

  getFoldersList(): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http
      .get<any>(`${this.prePath}/folders/`, {headers: headers});
  }

  addFolder(folder: any): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.post(`${this.prePath}/folders/`, folder, {headers: headers});
  }

  updateFolder(folder: any): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.put(`${this.prePath}/folders/${folder.folderId}`, folder, {headers: headers});
  }

  deleteFolder(folder: any): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.delete(`${this.prePath}/folders/` + folder.folderId, {headers: headers});
  }
}
