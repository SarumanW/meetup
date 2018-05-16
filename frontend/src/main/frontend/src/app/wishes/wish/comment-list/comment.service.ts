import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class CommentService {

  constructor(private http: HttpClient) {}

  //todo link with actual backend

  add(idItem : number, commentText:string): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.post<any>('api/comment/'+idItem, commentText, {headers:headers}).map(comment => {
      console.log(comment);
      return comment;
    });
  }

  getAll(idItem: number): Observable<any[]> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get<any>('api/comment/'+idItem+'/comments', {headers:headers})
      .map(comments => {
        return comments;
      })
  }

  destroy(idComment):Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.delete<any>('api/comment/'+idComment, {headers:headers})
      .map(comment => {
        return comment;
      })
  }
}
