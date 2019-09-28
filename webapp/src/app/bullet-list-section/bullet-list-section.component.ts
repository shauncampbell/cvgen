import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BulletListItem} from "../bullet-list/bullet-list.component";

@Component({
  selector: 'app-bullet-list-section',
  templateUrl: './bullet-list-section.component.html',
  styleUrls: ['./bullet-list-section.component.css']
})
export class BulletListSectionComponent implements OnInit {
  @Input() shouldShow: boolean;
  @Input() entries: Array<BulletListItem>;
  @Input() title: string;
  @Input() name: string;
  @Output() visibilityToggled = new EventEmitter<string>();

  constructor() { }

  ngOnInit() {
  }

}
