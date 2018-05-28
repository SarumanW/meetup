import {Router} from "@angular/router";
import {Component, OnInit} from '@angular/core';
import {Item} from "../item";
import {UploadFileService} from "../../upload.file/upload.file.service";
import {NgxSpinnerService} from "ngx-spinner";
import {WishListService} from "../wish.list.service";
import {ToastrService} from "ngx-toastr";
import {Profile} from "../../account/profile";
import {WishService} from "../wish.service";
import {ActivatedRoute} from '@angular/router';
import {CommentService} from "./comment-list/comment.service";
import {FormControl} from "@angular/forms";
import {ItemComment} from "./comment-list/comment";
import {AccountService} from "../../account/account.service";

@Component({
  selector: 'app-wish',
  templateUrl: './wish.component.html',
  styleUrls: ['./wish.component.css']
})
export class WishComponent implements OnInit {
  state: string = "wishes";
  success: boolean;
  item: Item;
  name = "ITEM";
  profile: Profile;
  idItem: number;
  login: string;
  ownerLogin:string;
  private sub: any;
  comments: ItemComment[];
  commentControl = new FormControl();
  isSubmitting = false;
  minDueDate: string;
  dueDate: string;
  priority: string;
  loginLikes: any;

  constructor(private router: Router,
              private spinner: NgxSpinnerService,
              private uploadService: UploadFileService,
              private toastr: ToastrService,
              private wishListService: WishListService,
              private commentsService: CommentService,
              private wishService: WishService,
              private route: ActivatedRoute,
              private accountService: AccountService) {
  }

  ngOnInit() {
    this.spinner.show();
    this.sub = this.route.params.subscribe(params => {
      this.idItem = +params['itemId'];
      this.login = params['login']
    });

    this.getItem(this.idItem);

    this.profile = JSON.parse(localStorage.getItem('currentUser'));

    this.populateComments();

  }

  populateComments() {
    this.commentsService.getAll(this.idItem)
      .subscribe(comments => this.comments = comments);
  }

  addComment() {
    this.isSubmitting = true;

    const commentBody = this.commentControl.value;
    var createdComment = new ItemComment();
    createdComment.bodyText = commentBody;
    this.spinner.show();
    this.commentsService
      .add(this.idItem, createdComment)
      .subscribe(
        comment => {
          this.comments.unshift(comment);
          this.commentControl.reset('');
        },
        errors => {

          this.showError('Unsuccessful comment adding. The length of comment must be less than 2000 symbols', 'Posting comment error');
        }
      );
    this.spinner.hide();
    this.isSubmitting = false;
  }

  onDeleteComment(commentId) {
    this.commentsService.destroy(commentId)
      .subscribe(
        success => {
          this.comments = this.comments.filter((item) => item.commentId !== commentId);
        }
      );
  }

  getLoginsWhoLiked(){
    this.wishService.getLoginsWhoLiked(this.idItem).subscribe((logins:string)=> {
        this.loginLikes = logins;
      }
    )
}

  like() {
    this.wishService.addLike(this.idItem).subscribe((item:Item)=>{
      this.item.likes = item.likes;
      this.item.like = item.like;
    })
  }

  dislike(){
    this.wishService.removeLike(this.idItem).subscribe((item:Item)=>{
      this.item.likes = item.likes;
      this.item.like = item.like;
    })
  }

  getItem(id: number) {
    this.wishService.getWishItem(id, this.login).subscribe(item => {
      this.item = item;
      this.accountService.getLoginById(this.item.ownerId).subscribe(
        login => {
          this.ownerLogin = login;
          this.spinner.hide();
        }, error => {
          this.ownerLogin = null;
          this.spinner.hide();
        }
      )
    });
  }


  removeFromWishList() {
    this.spinner.show();
    this.wishService.deleteWishItem(this.item).subscribe(deletedItem => {
      this.spinner.hide();
      this.showSuccess('Wish item was successfully deleted', 'Attention!');
      this.router.navigate(
        ['/'+ this.profile.login + '/wishes']);
    }, error => {
      this.showError('Unsuccessful wish item deleting', 'Adding error');
      this.spinner.hide();
    });
  }


  addToWishList() {
    let newItem = Object.assign({}, this.item);

    newItem.ownerId = this.profile.id;
    newItem.dueDate = this.dueDate + ' 00:00:00';
    newItem.priority = this.priority;

    this.spinner.show();
    this.wishService.addExistWishItem(newItem).subscribe(newItem => {
      this.spinner.hide();
      this.showSuccess('Wish item was successfully added', 'Attention!');
    }, error => {
      this.showError('Unsuccessful wish item adding', 'Adding error');
      this.spinner.hide();
    });
  }

  showSuccess(message: string, title: string) {
    this.toastr.info(message, title, {
      timeOut: 3000,
      positionClass: 'toast-top-right',
      closeButton: true
    });
  }

  showError(message: string, title: string) {
    this.toastr.error(message, title, {
      timeOut: 3000,
      positionClass: 'toast-top-right',
      closeButton: true
    });
  }

}
