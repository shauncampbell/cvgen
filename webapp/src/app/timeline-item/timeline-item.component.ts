import {Component, Input, OnInit} from '@angular/core';
import {BulletListItem} from "../bullet-list/bullet-list.component";

export interface TimelineItem {
  title: string;
  organisation: string;
  location: string;
  startDate: string;
  endDate: string;
  tags: Array<string>;
  bulletItems: Array<BulletListItem>;
}

@Component({
  selector: 'app-timeline-item',
  templateUrl: './timeline-item.component.html',
  styleUrls: ['./timeline-item.component.css']
})
export class TimelineItemComponent implements OnInit {
  @Input() item: TimelineItem;
  shouldShowDetails = true;
  constructor() { }

  ngOnInit() {
  }

}
