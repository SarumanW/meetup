import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpRequest} from "@angular/common/http";
import {Evento} from "./event";
import {Observable} from "rxjs/Observable";

@Injectable()
export class EventService {

  currentUser;
  prePath: string;

  constructor(private http: HttpClient) {
  }

  initPrePath() {
    this.currentUser = JSON.parse(localStorage.currentUser);
    this.prePath = `api/users/${this.currentUser.id}`;
  }

  getEvent(eventId: number): Observable<Evento> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http
      .get<any>(`${this.prePath}/events/` + eventId, {headers: headers});
  }

  addEvent(event: any): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.post(`${this.prePath}/events/`, event, {headers: headers});
  }

  updateEvent(eventt: Evento) {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.put(`${this.prePath}/events/${eventt.eventId}`, eventt, {headers: headers});
  }

  deleteEvent(eventt: Evento) {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.delete(`${this.prePath}/events/` + eventt.eventId, {headers: headers});
  }

  addParticipant(login: any, eventId: number): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.post(`${this.prePath}/events/${eventId}/participants`, login, {headers: headers});
  }

  deleteParticipants(eventt: Evento) {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.delete(`${this.prePath}/events/${eventt.eventId}/participants/`, {headers: headers});
  }

  deleteParticipant(eventt: Evento, participantIndex: number): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.delete(`${this.prePath}/events/${eventt.eventId}/participants/` + eventt.participants[participantIndex].id, {headers: headers});
  }

  getEventsByType(eventType: string, folderId: number): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.get(`${this.prePath}/events/folders/${folderId}/type/` + eventType, {headers: headers});
  }

  getDrafts(folderId: number): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.get(`${this.prePath}/events/folders/${folderId}/drafts`, {headers: headers});
  }

  getEventsInPeriod(startDate: string, endDate: string): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.get(`${this.prePath}/events/period`,
      {headers: headers, params: {'startDate': startDate, 'endDate': endDate}});
  }

  getPublicEvents(userId: number, queryField: string): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.get(`api/users/${userId}/events/public`,
      {headers: headers, params: {'name': queryField}});
  }

  pinEvent(userId: number, eventId: number): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.get(`${this.prePath}/events/${eventId}/pinned`, {headers: headers});
  }

  unpinEvent(userId: number, eventId: number): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.delete(`${this.prePath}/events/${eventId}/pinned`,
      {headers: headers});
  }

  uploadEventsPlan(data: any): Observable<any> {
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http.post(`${this.prePath}/events/plan/send`, data, {headers: headers});
  }

  getEventsByFolder(folderId : number) :  Observable<Evento[]>{
    this.initPrePath();

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${this.currentUser.token}`);

    return this.http
      .get<any>(`${this.prePath}/events/folder/` + folderId, {headers: headers});
  }

  getPlace(coords:string): Observable<any>{
    let lat = coords.split(' ')[0];
    let lng = coords.split(' ')[1];
    return this.http.get<any>("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=true&language=en")
  }

}
