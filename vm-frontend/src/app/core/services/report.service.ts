import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SummaryResponse } from 'src/app/core/models/summary-response';
import { TrendResponse } from 'src/app/core/models/trend-response';
@Injectable({
  providedIn: 'root'
})
export class ReportService {
  constructor(private http:HttpClient) { }
  private apiUrl='/api/admin/';
  getTrends(request:any){
    return this.http.post<TrendResponse>(this.apiUrl+"report"+"/"+"visitor"+"/"+"trends",request,{
      withCredentials:true
    })
  }
  getSummary(request:any){
    return this.http.post<SummaryResponse>(this.apiUrl+"report"+"/"+"visitor"+"/"+"summary",request,{
        withCredentials:true
      }
    )
  }
  exportVisitorCSV(request:any){
    return this.http.post(this.apiUrl+"visit"+"/"+"csv",request,{
      withCredentials:true,
      responseType:'blob'
    })
  }
}
