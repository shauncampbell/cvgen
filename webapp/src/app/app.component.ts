import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DataService} from "./data-service.service";

export interface DataConfigContent {
  forename: String;
  surname: String;
  about: Array<String>;
  skills: Array<DataConfigSkill>;
  contacts: DataConfigContacts;
  experience: Array<DataConfigExperience>;
  education: Array<DataConfigEducation>;
}

export interface DataConfigSkill {
  category: String;
  skills: Array<String>;
}

export interface DataConfigContacts {
  address: DataConfigAddress;
  telephone: Array<DataConfigNumber>;
  links: Array<Object>;
  email: Array<DataConfigEmail>;
}

export interface DataConfigEmail {
  type: String;
  recipient: String;
  domain: String;
}

export interface DataConfigNumber {
  type: String;
  number: {
    country: String;
    area: String;
    number: String;
  }
}

export interface DataConfigExperience {
  title: String;
  employer: {
    name: String;
    location: String;
  },
  startDate: String;
  endDate: String;
  mainDuties: Array<String>;
  skills: Array<String>;
}

export interface DataConfigEducation {
  title: String;
  institution: {
    name: String;
    location: String;
  },
  startDate: String;
  endDate: String;
  notes: Array<String>;
}

export interface DataConfigAddress {
  city: String;
  province: String;
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  data: DataConfigContent = {
      forename: "",
      surname: "",
      about: [],
      skills: [],
      contacts: {
        address: {
          city: "",
          province: ""
        },
        telephone: [],
        links: [],
        email: []
      },
      experience: [],
      education: []
  };

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.dataService.getContent().subscribe( (data: DataConfigContent) => {
      this.data = data
    });

  }
}
