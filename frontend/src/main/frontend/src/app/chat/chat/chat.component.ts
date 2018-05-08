import {Component, OnInit} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import * as $ from 'jquery';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  private serverUrl = 'http://localhost:8000/socket';
  private stompClient;

  state: string = "chat";
  message: string;

  constructor() {
    this.initializeWebSocketConnection();
  }

  ngOnInit() {
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(this.serverUrl);

    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function () {
      that.stompClient.subscribe("/chat", (message) => {
        if (message.body) {
          $(".chat").append("<div class='message'>" + message.body + "</div>")
          console.log(message.body);
        }
      }, (error) => {
        console.log("oops")
      });
    });
  }

  sendMessage(message) {
    this.stompClient.send("/app-chat/send/message", {}, message);
    $('#input').val('');
  }

}
