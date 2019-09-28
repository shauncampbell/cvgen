import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DataService} from "./data-service.service";
import {BulletListItem} from "./bullet-list/bullet-list.component";
import {TimelineItem} from "./timeline-item/timeline-item.component";

export interface DataConfigContent {
  forename: String;
  surname: String;
  about: Array<String>;
  skills: Array<DataConfigSkill>;
  hobbies: Array<String>;
  contacts: DataConfigContacts;
  experience: Array<DataConfigExperience>;
  education: Array<DataConfigEducation>;
  languages: Array<Language>;
}

export interface Language {
  language: string;
  note: string;
}

export interface Link {
  type: string;
  value: string;
}

export interface DataConfigSkill {
  category: string;
  skills: Array<string>;
}

export interface DataConfigContacts {
  address: DataConfigAddress;
  telephone: Array<DataConfigNumber>;
  links: Array<Link>;
  email: Array<DataConfigEmail>;
}

export interface DataConfigEmail {
  type: string;
  recipient: string;
  domain: string;
}

export interface DataConfigNumber {
  type: string;
  number: {
    country: string;
    area: string;
    number: string;
  }
}

export interface DataConfigExperience {
  title: string;
  employer: {
    name: string;
    location: string;
  },
  startDate: string;
  endDate: string;
  mainDuties: Array<string>;
  skills: Array<string>;
}

export interface DataConfigEducation {
  title: string;
  institution: {
    name: string;
    location: string;
  },
  startDate: string;
  endDate: string;
  notes: Array<string>;
}

export interface DataConfigAddress {
  city: string;
  province: string;
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  sectionsVisible = {
    "about": true,
    "skills": true,
    "experience": true,
    "education": true,
    "languages": true,
    "interests": true
  };

  languages = new Array<BulletListItem>();
  skills = new Array<BulletListItem>();
  hobbies = new Array<BulletListItem>();
  experience = new Array<TimelineItem>();
  education = new Array<TimelineItem>();

  // aboutVisible = true;
  // skillsVisible = true;
  // experienceVisible = true;
  // educationVisible = true;
  // languagesVisible = true;
  // interestsVisible = true;
  data: DataConfigContent = {
      forename: "",
      surname: "",
      about: [],
      skills: [],
      hobbies: [],
      contacts: {
        address: {
          city: "",
          province: ""
        },
        telephone: [],
        links: [],
        email: []
      },
      languages: [],
      experience: [],
      education: []
  };

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.dataService.getContent().subscribe( (data: DataConfigContent) => {
      this.data = data;

      for (let entry of  data.languages) {
        this.languages.push({ title: entry.language, values: [ entry.note ]});
      }

      for (let entry of data.skills) {
        this.skills.push({ title: entry.category, values: entry.skills });
      }

      for (let entry of data.hobbies) {
        this.hobbies.push({ title: null, values: [ entry ]});
      }

      for (let entry of data.experience) {
        var duties: Array<BulletListItem> = new Array<BulletListItem>();
        for (let duty of entry.mainDuties) {
          duties.push({ title: null, values: [ duty ] });
        }

        this.experience.push( {
          title: entry.title,
          organisation: entry.employer.name,
          location: entry.employer.location,
          startDate: entry.startDate,
          endDate: entry.endDate,
          tags: entry.skills,
          bulletItems: duties
        });
      }

      for (let entry of data.education) {
        var notes: Array<BulletListItem> = new Array<BulletListItem>();
        for (let note of entry.notes) {
          notes.push({ title: null, values: [ note ]});
        }

        this.education.push( {
          title: entry.title,
          organisation: entry.institution.name,
          location: entry.institution.location,
          startDate: entry.startDate,
          endDate: entry.endDate,
          tags: null,
          bulletItems: notes
        });
      }
    });

  }

  toggleSection(section: String) {
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
}
