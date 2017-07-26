import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoordinateFieldComponent } from './coordinate-field.component';

describe('CoordinateFieldComponent', () => {
  let component: CoordinateFieldComponent;
  let fixture: ComponentFixture<CoordinateFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoordinateFieldComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoordinateFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
