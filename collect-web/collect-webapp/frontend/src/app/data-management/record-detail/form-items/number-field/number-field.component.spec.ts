import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberFieldComponent } from './number-field.component';

describe('NumberFieldComponent', () => {
  let component: NumberFieldComponent;
  let fixture: ComponentFixture<NumericFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NumberFieldComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
