import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NumericIntegerFieldComponent } from './numeric-integer-field.component';

describe('NumericIntegerFieldComponent', () => {
  let component: NumericIntegerFieldComponent;
  let fixture: ComponentFixture<NumericIntegerFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NumericIntegerFieldComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NumericIntegerFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
