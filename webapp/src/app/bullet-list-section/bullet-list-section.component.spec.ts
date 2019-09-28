import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BulletListSectionComponent } from './bullet-list-section.component';

describe('BulletListSectionComponent', () => {
  let component: BulletListSectionComponent;
  let fixture: ComponentFixture<BulletListSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BulletListSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BulletListSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
