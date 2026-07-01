import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserResponse } from '../models/user-response';
@Injectable({
  providedIn: 'root'
})
export class AdminService {
  constructor(private http:HttpClient) { }
  private apiUrl='/api/admin/user';
  public getUsers(page:number,size:number){
    return this.http.get<UserResponse>(`${this.apiUrl}?page=${page}&size=${size}`,{
      withCredentials:true
    });
  }
  public addUser(data:any){
    return this.http.post(this.apiUrl+"/register",data,{
      withCredentials:true
    })
  }
  public deleteUser(id:number){
    return this.http.delete<any>(`${this.apiUrl}/${id}`,{
      withCredentials:true
    })
  }
  public disableUser(id:number){
    return this.http.put<any>(`${this.apiUrl}/disable/${id}`,{
      withCredentials:true
    })
  }
  public enableUser(id:number){
    return this.http.put<any>(`${this.apiUrl}/enable/${id}`,{
      withCredentials:true
    })
  }
}
