import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpRequest} from "@angular/common/http";
import {Evento} from "./event";
import {Observable} from "rxjs/Observable";

@Injectable()
export class EventService {

  constructor(private http: HttpClient) {
  }

  getEvent(eventId: number): Observable<Evento> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get<any>('api/events/' + eventId, {headers: headers});

  }

  addParticipant(login: any, eventId: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.post('api/events/' + eventId + '/participant/add', login, {headers: headers});
  }

  getEventsByType(eventType: string, folderId: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get('api/events/' + folderId + '/getByType/' + eventType, {headers: headers});
  }

  getDrafts(folderId: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get('api/events/' + folderId + '/drafts', {headers: headers});
  }

  updateEvent(eventt: Evento) {

    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.put('api/events', eventt, {headers: headers});
  }

  deleteParticipants(eventt: Evento) {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.delete('api/events/participants/' + eventt.eventId, {headers: headers});
  }

  deleteEvent(eventt: Evento) {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.delete('api/events/' + eventt.eventId, {headers: headers});
  }

  deleteParticipant(eventt: Evento, login: string) : Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.delete('api/events/' + eventt.eventId + "/participant/" + login, {headers: headers});
  }

  getEventsInPeriod(startDate: string, endDate: string): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get('api/events/getInPeriod',
      {headers: headers, params: {'startDate': startDate, 'endDate': endDate}});
  }

  uploadEventsPlan(data: any): Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.post('api/events/sendEventPlan', data, {headers: headers});
  }

  getPublicEvents(userId : number, queryField: string) : Observable<any>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.get('api/events/' + userId + '/public',
      {headers: headers, params: {'name': queryField}});
  }

  pinEvent(userId : number, eventId: number): Observable<any> {
      let headers = new HttpHeaders()
        .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

      return this.http.get('api/events/'+ userId + '/event/' + eventId + '/pinned',{headers: headers});
  }

  unpinEvent(userId : number, eventId: number): Observable<any> {
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http.delete('api/events/'+ userId + '/event/' + eventId + '/pinned',
      {headers: headers});
  }
}
