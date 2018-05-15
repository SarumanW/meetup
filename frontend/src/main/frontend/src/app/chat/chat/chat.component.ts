import {Component, OnDestroy, OnInit, Renderer2, RendererFactory2, ViewEncapsulation} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import * as $ from 'jquery';
import {Profile} from "../../account/profile";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {

  serverUrl = 'http://localhost:8000/socket';
  stompClient;
  state: string = "chat";
  messageText: string;
  profile: Profile;
  chatId: number;
  eventId: number;
  currentUserLogin: string;
  folderId: number;
  ws: any;
  colors: any;
  color: string;
  disabled: boolean = true;

  isButtonHidden: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router) {

  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.currentUser);
    this.route.params.subscribe(params => {
      this.chatId = params['chatId'];
      this.eventId = params['eventId'];
      this.folderId = params['folderId'];
    });
    this.currentUserLogin = JSON.parse(localStorage.currentUser).login;

    this.colors = [
      '#2196F3', '#32c787', '#00BCD4', '#ff5652',
      '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
    ];

    this.connect();
  }

  connect() {
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {

    let userName = this.profile.login;

    this.isButtonHidden = true;

    this.ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(this.ws);
    let that = this;

    this.stompClient.connect({}, function () {
      that.stompClient.subscribe("/chat/" + that.chatId, (payload) => {

        let message = JSON.parse(payload.body);
        let messageElement;

        if (message.type === 'JOIN') {
          if (message.sender === that.currentUserLogin) {
            that.disabled = false;
          }
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
          that.color = that.colors[ChatComponent.hashCode(message.sender) % 8];

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
  background-color:${that.color}";>${message.sender[0]}</i>
    <span style="color: #333;
  font-weight: 600;">${message.sender}</span>
    <p style="color: #43464b;">${message.content}</p>
</li>`;
        }

        $('#messageArea').append(messageElement);
        $('#messageArea').scrollTop($('#messageArea').prop('scrollHeight'));
      })
    });

    setTimeout(() => {
      this.stompClient.send("/app-chat/add/" + this.chatId, {}, JSON.stringify({sender: userName, type: 'JOIN'}));
    }, 3000)
  }

  sendMessage() {
    let chatMessage = {
      sender: this.profile.login,
      content: this.messageText,
      type: 'CHAT'
    };

    this.stompClient.send("/app-chat/send/message/" + this.chatId, {}, JSON.stringify(chatMessage));

    $('#message').val('');
  }

  backToEvent() {
    this.ngOnDestroy();
    this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId + "/event/" + this.eventId]);
  }

  static hashCode(name: string){
    let hash = 0;
    if (name.length == 0) return hash;
    for (let i = 0; i < name.length; i++) {
      let char = name.charCodeAt(i);
      hash = ((hash<<5)-hash)+char;
      hash = hash & hash; // Convert to 32bit integer
    }
    return Math.abs(hash);
  }

  ngOnDestroy() {
    if (this.stompClient !== undefined) {
      let chatMessage = {
        sender: this.profile.login,
        type: 'LEAVE'
      };
      this.stompClient.send("/app-chat/send/message/" + this.chatId, {}, JSON.stringify(chatMessage));
      this.ws.close();
    }
  }
}
