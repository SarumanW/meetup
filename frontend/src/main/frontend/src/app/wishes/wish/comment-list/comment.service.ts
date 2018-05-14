import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class CommentService {

  constructor(private http: HttpClient) {}

  //todo link with actual backend

  add(idItem : number, commentText:string): Observable<any> {
    return null;
    // return this.apiService
    //   .post(
    //     `/articles/${slug}/comments`,
    //     { comment: { body: payload } }
    //   ).pipe(map(data => data.comment));
  }

  getAll(idItem: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get<any>('api/comment/'+idItem+'/friends', {headers:headers})
      .map(comments => {
        return comments;
      })
  }

  destroy(idComment, idItem):Observable<any> {
    return null;
    // return this.apiService
    //   .delete(`/articles/${articleSlug}/comments/${commentId}`);
  }
}
