import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableFormItemComponent } from './table-form-item.component';

describe('TableFormItemComponent', () => {
  let component: TableFormItemComponent;
  let fixture: ComponentFixture<TableFormItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableFormItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableFormItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
