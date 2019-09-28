import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BulletListItem} from "../bullet-list/bullet-list.component";
import {TimelineItem} from "../timeline-item/timeline-item.component";

@Component({
  selector: 'app-timeline-section',
  templateUrl: './timeline-section.component.html',
  styleUrls: ['./timeline-section.component.css']
})
export class TimelineSectionComponent implements OnInit {
  @Input() shouldShow: boolean;
  @Input() entries: Array<TimelineItem>;
  @Input() title: string;
  @Input() name: string;
  @Output() visibilityToggled = new EventEmitter<string>();
  constructor() { }

  ngOnInit() {
  }

}
