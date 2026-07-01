import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginResponse } from '../models/login-response';
import { Observable } from 'rxjs';
import { ForgotPassword } from '../models/forgot-password.model';
import { ResetPassword } from '../models/reset-password.model';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private role:string='';
  constructor(private http:HttpClient) { }
  private apiUrl='/api/auth/';
  public login(data:any){
    return this.http.post(this.apiUrl+'login',data,{
      withCredentials:true
    });
  }
  public getUser():Observable<any>{
    return this.http.get<LoginResponse>(this.apiUrl+'me',{
      withCredentials:true
    });
  }
  public logout(){
    return this.http.post(this.apiUrl+'logout',{},{
      withCredentials:true
    })
  }
  public getCsrf(){
    return this.http.get('/api/csrf',{
      withCredentials:true
    })
  }
  public setRole(role:string){
    this.role=role;
  }
  public getRole(){
    return this.role;
  }
  public verifyUser(data: ForgotPassword) {
    return this.http.post(`${this.apiUrl}/verify-user`,data,{
      withCredentials:true
    })
  }
  public resetPassword(data:ResetPassword) {
    return this.http.post(`${this.apiUrl}/reset-password`,data,{
      withCredentials:true
    })
  }
}
