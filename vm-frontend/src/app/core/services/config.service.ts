import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  constructor(private http:HttpClient) { }
  private apiUrl='/api/admin/config';
  getConfig():Observable<any>{
    return this.http.get(this.apiUrl,{
      withCredentials:true
    });
  }
  updateConfig(data:any){
    return this.http.put(this.apiUrl,data,{
      withCredentials:true
    })
  }
}
