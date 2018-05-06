import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EventEditComponent } from './event.edit.component';

describe('Event.EditComponent', () => {
  let component: Event.EditComponent;
  let fixture: ComponentFixture<Event.EditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Event.EditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Event.EditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
