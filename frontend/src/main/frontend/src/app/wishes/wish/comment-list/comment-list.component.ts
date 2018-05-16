import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import {ItemComment} from "./comment";

@Component({
  selector: 'comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent implements OnInit {

  @Input() comment: ItemComment;
  @Output() deleteComment = new EventEmitter<boolean>();

  userId: number;

  constructor() { }

  ngOnInit() {
    this.userId = JSON.parse(localStorage.getItem('currentUser')).id;
  }

  deleteClicked() {
    this.deleteComment.emit(true);
  }

}
