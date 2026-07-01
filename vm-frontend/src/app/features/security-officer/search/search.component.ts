import { Component, OnInit } from '@angular/core';
import { ThemeService } from 'src/app/core/services/theme.service';
import { VisitorService } from 'src/app/core/services/visitor.service';
@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  searchText: string = '';
  visits: any[] = []; // Stores all visits fetched from the backend
  filteredVisits: any[] = []; // Stores filtered visits for the table
  constructor(private visitorService: VisitorService,public themeService:ThemeService) {}
  ngOnInit(): void {
    this.fetchVisitRecords();
  }
  fetchVisitRecords(): void {
    this.visitorService.getVisitRecords().subscribe({
      next: (data) => {
        this.visits = data || [];
        console.log(data);
        this.filteredVisits = [...this.visits];
        console.log("Data passed to table: ",this.filteredVisits)
      },
      error: (err) => {
        console.error('Error in fetching visit records:', err);
      }
    });
  }
  onSearch(): void {
    const searchString = this.searchText.toLowerCase();
    this.filteredVisits = this.visits.filter(
      (visit) =>
        visit.visitorName.toLowerCase().startsWith(searchString) ||
        visit.uniqueId.toLowerCase().startsWith(searchString)
    );
  }
}
