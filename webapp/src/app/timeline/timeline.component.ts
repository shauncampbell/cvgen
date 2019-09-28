import {Component, Input, OnInit} from '@angular/core';
import {DataConfigContent} from "../app.component";
import {BulletListItem} from "../bullet-list/bullet-list.component";
import {TimelineItem} from "../timeline-item/timeline-item.component";

@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.css']
})
export class TimelineComponent implements OnInit {
  @Input() data: DataConfigContent;
  @Input() skills: Array<BulletListItem>;
  @Input() languages: Array<BulletListItem>;
  @Input() interests: Array<BulletListItem>;
  @Input() experience: Array<TimelineItem>;
  @Input() education: Array<TimelineItem>;

  sectionsVisible = {
    "about": true,
    "skills": true,
    "experience": true,
    "education": true,
    "languages": true,
    "interests": true
  };


  toggleSection(section: String) {
    console.log(section);
    switch (section) {
      case "about":
        this.sectionsVisible.about = !this.sectionsVisible.about;
        break;
      case "skills":
        this.sectionsVisible.skills = !this.sectionsVisible.skills;
        break;
      case "experience":
        this.sectionsVisible.experience = !this.sectionsVisible.experience;
        break;
      case "education":
        this.sectionsVisible.education = !this.sectionsVisible.education;
        break;
      case "languages":
        this.sectionsVisible.languages = !this.sectionsVisible.languages;
        break;
      case "interests":
        this.sectionsVisible.interests = !this.sectionsVisible.interests;
        break;
    }
  }

  constructor() { }

  ngOnInit() {
  }

}
