import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class ChatService {

  currentUser;
  prePath: string;

  constructor(private http: HttpClient) {}

  initPrePath() {
    this.currentUser = JSON.parse(localStorage.currentUser);
    this.prePath = `api/users/${this.currentUser.id}`;
  }

  addChat(eventId: any):Observable<any>{
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.post(`${this.prePath}/chats`, eventId, {headers: headers});
  }

  getChatIds(eventId: any):Observable<any>{
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.get(`${this.prePath}/chats/` + eventId, {headers: headers});
  }

  addMessage(message: any):Observable<any>{
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.post(`${this.prePath}/chats/message`, message, {headers: headers});
  }

  getMessages(chatId: any): Observable<any>{
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.get(`${this.prePath}/chats/messages/` + chatId, {headers: headers});
  }

  getMembers(chatId: any): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.get(`${this.prePath}/chats/${chatId}/members/`, {headers: headers});
  }

  addMember(login: any, chatId: any): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.post(`${this.prePath}/chats/${chatId}/member`, login, {headers: headers});
  }

  deleteMember(login: any, chatId: any): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.delete(`${this.prePath}/chats/${chatId}/member/` + login, {headers: headers});
  }
}
