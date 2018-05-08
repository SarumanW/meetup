import {Component, OnInit, Renderer2, RendererFactory2, ViewEncapsulation} from '@angular/core';
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

  isButtonHidden: boolean = false;

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
        let color;

        if (message.type === 'JOIN') {
          message.content = message.sender + ' joined!';
          messageElement = `<li class="event-message" style="width: 100%;
  text-align: center;
  clear: both;">
    <p style="color: #777;
  font-size: 14px;
  word-wrap: break-word;">${message.content}</p>
</li>`;
        } else if (message.type === 'LEAVE') {
          message.content = message.sender + ' left!';
          messageElement = `<li class="event-message" style="width: 100%;
  text-align: center;
  clear: both;">
    <p style="color: #777;
  font-size: 14px;
  word-wrap: break-word;">${message.content}</p>
</li>`;
        } else {
          let index = Math.floor(Math.random() * colors.length);

          let color = colors[index];

          messageElement = `<li class="chat-message" style="padding-left: 68px;
  position: relative;">
    <i style="position: absolute;
  width: 42px;
  height: 42px;
  overflow: hidden;
  left: 10px;
  display: inline-block;
  vertical-align: middle;
  font-size: 18px;
  line-height: 42px;
  color: #fff;
  text-align: center;
  border-radius: 50%;
  font-style: normal;
  text-transform: uppercase;
  background-color:${color}";>${message.sender[0]}</i>
    <span style="color: #333;
  font-weight: 600;">${message.sender}</span>
    <p style="color: #43464b;">${message.content}</p>
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
}
