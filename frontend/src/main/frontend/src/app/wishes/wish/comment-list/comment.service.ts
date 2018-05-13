import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {map} from "rxjs/operator/map";

@Injectable()
export class CommentService {

  constructor(private http: HttpClient) {}

  //todo link with actual backend

  add(idItem, commentText): Observable<Comment> {
    return null;
    // return this.apiService
    //   .post(
    //     `/articles/${slug}/comments`,
    //     { comment: { body: payload } }
    //   ).pipe(map(data => data.comment));
  }

  getAll(idItem): Observable<Comment[]> {
    return null;
    // return this.apiService.get(`/articles/${slug}/comments`)
    //   .pipe(map(data => data.comments));
  }

  destroy(idComment, idItem) {

    // return this.apiService
    //   .delete(`/articles/${articleSlug}/comments/${commentId}`);
  }
}
