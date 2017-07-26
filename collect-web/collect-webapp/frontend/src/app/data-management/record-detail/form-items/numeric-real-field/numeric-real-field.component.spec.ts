import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NumericRealFieldComponent } from './numeric-real-field.component';

describe('NumericRealFieldComponent', () => {
  let component: NumericRealFieldComponent;
  let fixture: ComponentFixture<NumericRealFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NumericRealFieldComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NumericRealFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
