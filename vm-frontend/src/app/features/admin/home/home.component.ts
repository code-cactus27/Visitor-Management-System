import { Component } from '@angular/core';
import { ThemeService } from 'src/app/core/services/theme.service';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(public themeService: ThemeService){}
}
