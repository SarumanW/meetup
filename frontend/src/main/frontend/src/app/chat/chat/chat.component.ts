import {Component, OnInit, Renderer2, RendererFactory2} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import * as $ from 'jquery';
import {Profile} from "../../account/profile";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  serverUrl = 'http://localhost:8000/socket';
  stompClient;
  state: string = "chat";
  messageText: string;
  profile: Profile;
  color;

  isButtonHidden: boolean = false;

  colorss = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
  ];

  constructor() {

  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.currentUser);
  }

  connect() {
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {

    let userName = this.profile.login;

    this.isButtonHidden = true;

    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let that = this;

    this.stompClient.connect({}, function () {
      that.stompClient.subscribe("/chat", (payload) => {

        let colors = [
          '#2196F3', '#32c787', '#00BCD4', '#ff5652',
          '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
        ];

        let message = JSON.parse(payload.body);
        let messageElement;

        if (message.type === 'JOIN') {
          message.content = message.sender + ' joined!';
          messageElement = `<li class="event-message">
    <p>${message.content}</p>
</li>`;
        } else if (message.type === 'LEAVE') {
          message.content = message.sender + ' left!';
          messageElement = `<li class="event-message">
    <p>${message.content}</p>
</li>`;
        } else {
          this.color = colors[0];
          messageElement = `<li class="chat-message">
    <i [style.backround-color]='color'>${message.sender[0]}</i>
    <span>${message.sender}</span>
    <p>${message.content}</p>
</li>`;
        }

        $('#messageArea').append(messageElement);
        $('#messageArea').scrollTop = $('#messageArea').scrollHeight;
      })
    });

    setTimeout(() => {
      this.stompClient.send("/app-chat/add", {}, JSON.stringify({sender: userName, type: 'JOIN'}));
    }, 2000)
  }

  sendMessage() {
    let chatMessage = {
      sender: this.profile.login,
      content: this.messageText,
      type: 'CHAT'
    };

    this.stompClient.send("/app-chat/send/message", {}, JSON.stringify(chatMessage));

    $('#message').val('');
  }

  getAvatarColor(messageSender): number {
    let hash = 0;

    for (let i = 0; i < messageSender.length; i++) {
      hash = 31 * hash + messageSender.charCodeAt(i);
    }

    let index = Math.abs(hash % this.colorss.length);
    return index;
  }
}
