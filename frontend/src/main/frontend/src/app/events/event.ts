import {Profile} from "../account/profile";
export class Evento{
  eventId : number;
  name : string;
  eventDate : string;
  description : string;
  periodicityId : number;
  ownerLogin: string;
  periodicity : string;
  place : string;
  eventTypeId : number;
  eventType : string;
  isDraft : boolean;
  folderId : number;
  imageFilepath : string;
  isPinned: boolean;
  ownerId : number;
  participants : Profile[]
  privateChatId: number;
  publicChatId: number;

  clone() : Evento {
    let itemClone = new Evento();

    itemClone.eventId = this.eventId;
    itemClone.name = this.name;
    itemClone.eventDate = this.eventDate;
    itemClone.periodicity = this.periodicity;
    itemClone.place = this.place;
    itemClone.eventTypeId = this.eventTypeId;
    itemClone.eventType = this.eventType;
    itemClone.isDraft = this.isDraft;
    itemClone.folderId = this.folderId;
    itemClone.ownerId = this.ownerId;
    itemClone.imageFilepath = this.imageFilepath;
    itemClone.isPinned = this.isPinned;
    itemClone.imageFilepath = this.imageFilepath;
    itemClone.participants = this.participants;

    return itemClone;
  }
}
