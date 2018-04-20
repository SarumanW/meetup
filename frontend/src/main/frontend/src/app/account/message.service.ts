import { Injectable } from '@angular/core';

@Injectable()
export class MessageService {
  message: string;

  clear() {
    this.message = "";
  }
}
