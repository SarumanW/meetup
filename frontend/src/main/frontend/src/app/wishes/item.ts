import {Tag} from "./tag";

export class Item {
  itemId: number;
  name: string;
  description: string;
  ownerId: number;
  bookerId: number;
  priority: string;
  imageFilepath: string;
  link: string;
  dueDate: string;
  likes: number;
  tags: string[] = [];
}

