import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class VisitorService {
  url: string = "/gate/visitor";
  private selectedId: string = '';
  constructor(private httpClient: HttpClient) { }
  getVisitors(): Observable<any> {
    return this.httpClient.get(this.url + "s");
  }
  getOneVisitor(id: any): Observable<any> {
    return this.httpClient.get(this.url + "/" + id);
  }
  updateVisitor(uniqueId: string, data: any) {
    return this.httpClient.put(this.url + "/" + uniqueId, data,{responseType:'text'});
  }
  addVisitor(data: any) {
    return this.httpClient.post("/gate/add", data);
  }
  addVisitNew(data: any): Observable<any> {
    return this.httpClient.post("/gate/visit/new", data);
  }
  addVisitExisting(data: any): Observable<any> {
    return this.httpClient.post("/gate/visit/existing", data);
  }
  getVisitsByVisitor(uniqueId:string): Observable<any[]>{
    return this.httpClient.get<any[]>('/gate/visit/visitor/'+uniqueId);
  }
  getVisitRecords() {
    return this.httpClient.get<any[]>('/gate/visit/all');
  }
  getDashboard(){
    return this.httpClient.get<any>('/gate/dashboard');
  }
  approveVisitor(id:number){
    return this.httpClient.put(`/gate/approve/${id}`,{},{responseType:'text'});
  }
  rejectvisitor(id:number){
    return this.httpClient.put(`/gate/reject/${id}`,{},{responseType:'text'})
  }
  checkOutVisitor(id:number){
    return this.httpClient.put(`/gate/checkout/${id}`,{},{responseType:'text'})
  }
  deleteVisitor(uniqueId:number){
    return this.httpClient.delete(this.url+"/"+uniqueId,{responseType:'text'})
  }
}
