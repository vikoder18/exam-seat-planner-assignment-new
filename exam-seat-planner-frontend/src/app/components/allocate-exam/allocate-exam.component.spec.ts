import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllocateExamComponent } from './allocate-exam.component';

describe('AllocateExamComponent', () => {
  let component: AllocateExamComponent;
  let fixture: ComponentFixture<AllocateExamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllocateExamComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllocateExamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
