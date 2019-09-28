import {Component, Input, OnInit} from '@angular/core';

export interface BulletListItem {
  title: string;
  values: Array<String>;
}

@Component({
  selector: 'app-bullet-list',
  templateUrl: './bullet-list.component.html',
  styleUrls: ['./bullet-list.component.css']
})
export class BulletListComponent implements OnInit {
  @Input() entries: Array<BulletListItem>;
  constructor() { }

  ngOnInit() {
  }

}
