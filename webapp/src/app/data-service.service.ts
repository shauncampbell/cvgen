import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  configUrl = 'api/v1/content';

  constructor(private http: HttpClient) { }

  getContent() {
    return this.http.get(this.configUrl);
  }
}
