import { Component } from '@angular/core';
import { ReportService } from 'src/app/core/services/report.service';
import { ThemeService } from 'src/app/core/services/theme.service';
@Component({
  selector: 'app-visitor-report-export',
  templateUrl: './visitor-report-export.component.html',
  styleUrls: ['./visitor-report-export.component.css']
})
export class VisitorReportExportComponent {
  constructor(private reportService:ReportService, public themeService: ThemeService){
  }
  startDate!:string;
  endDate!:string;
  loading=false;
  onDownload():void{
    this.loading=true;
    this.reportService.exportVisitorCSV({
      startDate:this.startDate+"T00:00:00",
      endDate:this.endDate+"T23:59:59"
    }).subscribe((blob)=>{
      const filename=`visits_report_${this.startDate}_to_${this.endDate}.csv`;
      const link=document.createElement('a');
      const url=URL.createObjectURL(blob);
      link.href=url;
      link.download=filename;
      link.click();
      URL.revokeObjectURL(url);
      this.loading=false;
    },(error)=>{
      alert('Failed to generate report. Please try again.');
    })
  }
}
