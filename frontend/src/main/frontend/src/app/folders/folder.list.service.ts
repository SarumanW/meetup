import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import {Folder} from "./folder";
import {FOLDERS} from "./mock-folders";

@Injectable()
export class FolderListService {

  constructor() { }

  getFolders(): Observable<Folder[]> {
    return of(FOLDERS);
  }
}
