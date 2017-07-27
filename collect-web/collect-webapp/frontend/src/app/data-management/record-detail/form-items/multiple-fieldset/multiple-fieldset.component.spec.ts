import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultipleFieldsetComponent } from './multiple-fieldset.component';

describe('MultipleFieldsetComponent', () => {
  let component: MultipleFieldsetComponent;
  let fixture: ComponentFixture<MultipleFieldsetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultipleFieldsetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultipleFieldsetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
