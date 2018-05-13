import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';

@Component({
  selector: 'comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent implements OnInit {

  @Input() comment: Comment;
  @Output() deleteComment = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit() {
  }

  deleteClicked() {
    this.deleteComment.emit(true);
  }

}
