import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BulletListItem} from "../bullet-list/bullet-list.component";

@Component({
  selector: 'app-text-section',
  templateUrl: './text-section.component.html',
  styleUrls: ['./text-section.component.css']
})
export class TextSectionComponent implements OnInit {
  @Input() shouldShow: boolean;
  @Input() entries: Array<string>;
  @Input() title: string;
  @Input() name: string;
  @Output() visibilityToggled = new EventEmitter<string>();
  constructor() { }

  ngOnInit() {
  }

}
