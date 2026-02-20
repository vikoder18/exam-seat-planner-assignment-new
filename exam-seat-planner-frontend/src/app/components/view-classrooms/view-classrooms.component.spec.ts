import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewClassroomsComponent } from './view-classrooms.component';

describe('ViewClassroomsComponent', () => {
  let component: ViewClassroomsComponent;
  let fixture: ComponentFixture<ViewClassroomsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewClassroomsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewClassroomsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
