import { Component, OnInit } from '@angular/core';
import { ReportService } from 'src/app/core/services/report.service';
import { ThemeService } from 'src/app/core/services/theme.service';
import { TrendData } from 'src/app/core/models/trend-data';
@Component({
  selector: 'app-report-dashboard',
  templateUrl: './report-dashboard.component.html',
  styleUrls: ['./report-dashboard.component.css']
})
export class ReportDashboardComponent implements OnInit{
  constructor(private reportService:ReportService, public themeService: ThemeService){
  }
  selectedRange='7days';
  selectedType='daily';
  rangeOptions:any[]=[];
  trendData:TrendData[]=[];
  avgDuration='00:00:00';//dummy
  ngOnInit(): void {
    this.onTypeChange();
  }
  onTypeChange(){
    this.selectedRange='';
    this.updateRangeOptions();
    this.onFilterChange();
  }
  onFilterChange(){
    const{startDate,endDate}=this.getDateRange();
    const request={
      type:this.selectedType,
      startDate,
      endDate
    };
// console.log('API REQUEST:',request);
    this.reportService.getTrends(request).subscribe(res=>{
      this.trendData=res.data.map(item=>({
        label:this.formatLabel(item.label),
        count:item.count
      }));
    })
    this.reportService.getSummary(request).subscribe(res=>{
      this.avgDuration=res.avgVisitDuration;
    })
  }
  formatLabel(value:string):string{
    if(this.selectedType==='daily'){
      const date=new Date(value);
      return date.toLocaleDateString('en-US',{weekday:'short'});
    }
    if(this.selectedType==='monthly'){
      const date=new Date(value+'-01');
      return date.toLocaleDateString('en-US',{month:'short'});
    }
    if(this.selectedType==='weekly'){
      return this.formatWeek(value);
    }
    return value;
  }
  formatWeek(value:string){
    const week=value.toString().slice(4);
    return `Week ${week}`;
  }
  updateRangeOptions(){
    if(this.selectedType==='daily'){
      this.rangeOptions=[
        {
          label:'Last 7 days',value:'7days'
        }
      ];
    }
    if(this.selectedType==='weekly'){
      this.rangeOptions=[
        {
          label:'Last 4 Weeks',value:'4weeks'
        },
        {
          label:'Last 12 Weeks',value:'12weeks'
        }
      ];
    }
    if(this.selectedType==='monthly'){
      this.rangeOptions=[
        {
          label:'Last 6 Months',value:'6months'
        },
        {
          label:'Last 12 Months',value:'12months'
        }
      ];
    }
    if(!this.selectedRange){
      this.selectedRange=this.rangeOptions[0].value;
    }
  }
  getDateRange(){
    const end=new Date();
    const start=new Date();
    switch(this.selectedRange){
      case '7days':
        start.setDate(end.getDate()-7);
        break;
      case '30days':
        start.setDate(end.getDate()-30);
        break;
      case '4weeks':
        start.setDate(end.getDate()-28);
        break;
      case '12weeks':
        start.setDate(end.getDate()-84);
        break;
      case '6months':
        start.setDate(end.getMonth()-183);
        break;
      case '12months':
        start.setDate(end.getMonth()-365);
        break;
    }
    return {
      startDate:start.toISOString(),
      endDate:end.toISOString()
    };
  }
  get maxValue():number{
    return Math.max(...this.trendData.map(d=>d.count),1)
  }
  get totalVisitors():number{
    return this.trendData.reduce((sum,item)=>sum+item.count,0)
  }
  onExport(){
    console.log('Export clicked');
  }
}
