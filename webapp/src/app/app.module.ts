import { NgModule }         from '@angular/core';
import { BrowserModule }    from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { TimelineComponent } from './timeline/timeline.component';
import { BulletListComponent } from './bullet-list/bullet-list.component';
import { BulletListSectionComponent } from './bullet-list-section/bullet-list-section.component';
import { TimelineItemComponent } from './timeline-item/timeline-item.component';
import { TimelineSectionComponent } from './timeline-section/timeline-section.component';
import { TextSectionComponent } from './text-section/text-section.component';


@NgModule({
  declarations: [
    AppComponent,
    TimelineComponent,
    BulletListComponent,
    BulletListSectionComponent,
    TimelineItemComponent,
    TimelineSectionComponent,
    TextSectionComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

