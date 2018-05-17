import {Component, OnDestroy, OnInit, Renderer2, RendererFactory2, ViewEncapsulation} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import * as $ from 'jquery';
import {Profile} from "../../account/profile";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatService} from "../chat.service";
import {Message} from "../message";
import {forEach} from "@angular/router/src/utils/collection";
import {NgxSpinnerService} from "ngx-spinner";
import {EventService} from "../../events/event.service";
import {Evento} from "../../events/event";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {

  serverUrl = 'http://localhost:8000/socket';
  stompClient;
  state: string = "folders";
  messageText: string;
  profile: Profile;
  chatId: number;
  event: Evento;
  currentUserLogin: string;
  folderId: number;
  ws: any;
  colors: any;
  color: string;
  disabled: boolean = true;
  preventMessages: Message[] = [];
  chatMembers: Profile[] = [];

  isTyping: boolean = false;
  typingMemberText: string = '';
  typingMembers: string[] = [];

  isButtonHidden: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private chatService: ChatService,
              private eventService: EventService,
              private spinner: NgxSpinnerService,
              private appComponent:AppComponent,) {

  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.currentUser);

    let eventId = 0;

    this.route.params.subscribe(params => {
      this.chatId = params['chatId'];
      eventId = params['eventId'];
      this.folderId = params['folderId'];
    });
    this.currentUserLogin = JSON.parse(localStorage.currentUser).login;

    this.colors = [
      '#2196F3', '#32c787', '#00BCD4', '#ff5652',
      '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
    ];

    this.spinner.show();

    this.eventService.getEvent(eventId).subscribe(
      event => {
        this.event = event;
      }, error => this.appComponent.showError(error, "Error"));

    this.chatService.getMessages(this.chatId).subscribe(
      (messages) => {
        this.preventMessages = messages;

        for (let message of messages) {
          let messageElement;
          let color = this.colors[ChatComponent.hashCode(message.senderId) % 8];
          let time = this.getTimeFromDate(message.messageDate);

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
  background-color:${color}";>${message.senderLogin[0]}</i>
  <span style="color: #333;
  font-weight: 600;">${time}</span>
    <span style="color: #333;
  font-weight: 600;">${message.senderLogin}</span>
    <p style="color: #43464b;">${message.text}</p>
</li>`;
          $('#messageArea').append(messageElement);
        }

        $('#messageArea').scrollTop($('#messageArea').prop('scrollHeight'));

        this.spinner.hide();

        this.connect();
      });
  }

  connect() {
    this.initializeWebSocketConnection();

    this.chatMembers.push(this.profile);
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
          that.stopTypingMember(message.sender);
        } else if (message.type === 'TYPING') {
          console.log(that.typingMembers.indexOf(message.sender));

          const memberIndex = that.typingMembers.indexOf(message.sender);

          if (memberIndex === -1) {
            that.typingMembers.push(message.sender);
          }

          console.log(that.typingMembers);

          that.typingsMembersNotification();

        } else if (message.type === 'NOT_TYPING') {
          that.stopTypingMember(message.sender);
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
          that.stopTypingMember(message.sender);
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

    let mess: Message = new Message();
    mess.chatId = this.chatId;
    mess.text = this.messageText;
    mess.senderId = this.profile.id;
    mess.messageDate = this.getCurrentDate();

    this.chatService.addMessage(mess).subscribe(
      (message) => {
        console.log(message);
        this.stompClient.send("/app-chat/send/message/" + this.chatId, {}, JSON.stringify(chatMessage));
      }
    );

    this.messageText = '';
    this.isUserTypingMessage();
  }

  // USER TYPE TEXT EVENTS

  isUserTypingMessage() {
    console.log(this.messageText);
    console.log(this.typingMembers);
    if(this.messageText === '' && this.isTyping) {
      this.isTyping = false;
      this.sendIsTypingMember('NOT_TYPING');
    } else if (!this.isTyping) {
      this.isTyping = true;
      this.sendIsTypingMember('TYPING');
    }
  }

  sendIsTypingMember(messageType: string) {
    let chatMessage = {
      sender: this.profile.login,
      type: messageType
    };

    this.stompClient.send("/app-chat/send/message/" + this.chatId, {}, JSON.stringify(chatMessage));
  }

  stopTypingMember(sender: string) {
    const memberIndex = this.typingMembers.indexOf(sender);

    if (memberIndex !== -1) {
      this.typingMembers.splice(memberIndex, 1);
    }

    this.typingsMembersNotification();
  }

  typingsMembersNotification() {
    if (this.typingMembers.length === 0) {
      this.typingMemberText = '';
    } else {
      this.typingMemberText = '';
      let that = this;

      this.typingMembers.forEach(function (member) {
        that.typingMemberText += member + ', ';
      });

      this.typingMemberText = this.typingMemberText.replace(new RegExp(', ' + '$'), ' ');

      if (this.typingMembers.length === 1 && this.typingMembers[1] !== this.profile.login) {
        this.typingMemberText += 'is typing ...';
      } else {
        this.typingMemberText += 'are typing ...'
      }

      console.log(this.typingMemberText);
    }
  }

  backToEvent() {
    this.ngOnDestroy();
    this.router.navigate(["/" + this.currentUserLogin + "/folders/" + this.folderId + "/event/" + this.event.eventId]);
  }

  static hashCode(name: string) {
    let hash = 0;
    if (name.length == 0) return hash;
    for (let i = 0; i < name.length; i++) {
      let char = name.charCodeAt(i);
      hash = ((hash << 5) - hash) + char;
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

  getCurrentDate(): string {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let min = date.getMinutes();
    let sec = date.getSeconds();
    let time = hour + ":" + min + ":" + sec;
    console.log(year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + " " + time);
    return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + " " + time;
  }

  getTimeFromDate(date: string): string {
    let time = date.split(" ")[1];
    time = time.substr(0, 8);
    return time;
  }
}
