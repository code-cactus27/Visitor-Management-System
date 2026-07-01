import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private apiUrl = 'http://localhost:8765/feedback';
  constructor(private http:HttpClient){ }
  submitFeedback(data:any){
    return this.http.post(this.apiUrl+"/submit", data,{
      responseType:'text'
    });
  }
  getAllFeedbacks() {
    return this.http.get<any[]>(
      this.apiUrl + "/all"
    );
  }
}
