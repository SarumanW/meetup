import {Profile} from "../account/profile";
export class Evento{
  eventId : number;
  name : string;
  eventDate : string;
  description : string;
  periodicityId : number;
  periodicity : string;
  place : string;
  eventTypeId : number;
  eventType : string;
  isDraft : boolean;
  folderId : number;
  imageFilepath : string;
  ownerId : number;
  participants : Profile[]
}
