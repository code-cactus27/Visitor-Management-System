// import { Component, OnInit } from '@angular/core';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { AuthService } from 'src/app/core/services/auth.service';
// import { Router } from '@angular/router';
// import { ThemeService } from 'src/app/core/services/theme.service';
// import { FeedbackService } from 'src/app/core/services/feedback.service';
//
// @Component({
//   selector: 'app-login',
//   templateUrl: './login.component.html',
//   styleUrls: ['./login.component.css']
// })
// export class LoginComponent implements OnInit{
//
//   errorMessage:string='';
//   loginForm: FormGroup;
//   feedbacks:any[]=[];
//
//   isLoading:boolean=false;
//
//   dosList: string[]=[
//     'Carry your valid ID card / visitor pass at all times.',
//     'Maintain cleanliness inside the campus premises.',
//     'Follow security instructions and campus guidelines.',
//     'Use designated parking and visitor waiting areas.',
//     'Respect campus property and public facilities.',
//     'Keep mobile phones on silent mode in office areas.',
//     'Report any suspicious activities to security staff.',
//     'Use eco-friendly practices and avoid littering.'
//   ];
//
//   dontsList: string[] = [
//     'Smoking and alcohol are strictly prohibited.',
//     'Do not enter restricted or unauthorized areas.',
//     'Avoid loud noise and disturbance inside the campus.',
//     'Do not damage campus property or infrastructure.',
//     'Outside food is not allowed in certain office zones.',
//     'Do not raise false alarms or emergency alerts.',
//     'Photography may be restricted in secure areas.',
//     'Do not share confidentail company information.'
//   ];
//
//
//   constructor(private fb: FormBuilder,private authService:AuthService,private router:Router, public themeService:ThemeService,
//               private feedbackSerice:FeedbackService){
//     this.loginForm = this.fb.group({
//       email: ['', [Validators.required, Validators.email]],
//       password: ['', [Validators.required, Validators.minLength(6),Validators.pattern('^[A-Z][a-z]+[@$!%*?&]+\\d+$')]]
//     });
//   }
//
//   ngOnInit() {
//     this.loadFeedbacks();
//   }
//
//   loadFeedbacks() {
//     this.feedbackSerice.getAllFeedbacks().subscribe({
//       next:(res)=>{
//         this.feedbacks = res.reverse();
//       },
//       error:(err)=>{
//         console.log(err);
//       }
//     });
//   }
//
//   onLogin(){
//     this.errorMessage='';
//     this.isLoading=true;
//     this.authService.login(this.loginForm.value)
//       .subscribe(()=>{
//
//         this.authService.getUser().subscribe(res=>{
//
// // this.authService.getCsrf().subscribe(res=>{
//
// // })
//           const role=res.role;
//           this.authService.setRole(role);
//           if(role=='ADMIN'){
//             this.router.navigate(['/admin']);
//           }
//           else if(role=='RECEPTIONIST'){
//             this.router.navigate(['/receptionist'])
//
//           }
//           else{
//             this.router.navigate(['/security'])
//           }
//
//           this.isLoading=false;
//         })
//       },(err)=>{
//         this.isLoading=false;
//         console.log(err);
//         this.errorMessage=
//           err.error?.errorMessage|| "Login failed";
//
//         setTimeout(() => {
//           this.errorMessage='';
//         },2000);
//
//       })
//
//   }
//
// }
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { ThemeService } from 'src/app/core/services/theme.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMessage: string = '';
  loginForm: FormGroup;
  isLoading: boolean = false;

  // ─── Do's & Don'ts ───────────────────────────────────────────
  dosList: string[] = [
    'Carry a valid government-issued photo ID at all times.',
    'Register at the reception desk upon arrival.',
    'Wear the visitor badge visibly throughout your visit.',
    'Follow all campus security instructions.',
    'Inform the receptionist before moving to a different block.',
    'Respect the privacy and workspace of employees.'
  ];

  dontsList: string[] = [
    'Do not enter restricted or unauthorized areas.',
    'Do not carry cameras or recording devices without permission.',
    'Do not share your visitor pass with anyone else.',
    'Do not leave your belongings unattended.',
    'Do not engage with employees in non-designated areas.',
    'Do not violate the no-smoking policy inside the campus.'
  ];

  // ─── Feedback data ───────────────────────────────────────────
  // Loaded from API; falls back to static data if API is unavailable
  feedbacks: any[] = [];

  private staticFeedbacks = [];

  constructor(
    private fb:          FormBuilder,
    private authService: AuthService,
    private router:      Router,
    public  themeService: ThemeService,
    private http:        HttpClient
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(6),
        Validators.pattern('^[A-Z][a-z]+[@$!%*?&]+\\d+$')
      ]]
    });
  }

  ngOnInit(): void {
    this.loadFeedbacks();
  }

  private loadFeedbacks(): void {
    this.http.get<any[]>('http://localhost:8765/feedback/all').subscribe({
      next:  (data) => { this.feedbacks = data?.length ? data : this.staticFeedbacks; },
      error: ()     => { this.feedbacks = this.staticFeedbacks; }
    });
  }

  onLogin(): void {
    this.errorMessage = '';
    this.isLoading    = true;

    this.authService.login(this.loginForm.value).subscribe(
      () => {
        this.authService.getUser().subscribe(res => {
          const role = res.role;
          this.authService.setRole(role);
          if      (role === 'ADMIN')        this.router.navigate(['/admin']);
          else if (role === 'RECEPTIONIST') this.router.navigate(['/receptionist']);
          else                              this.router.navigate(['/security']);
          this.isLoading = false;
        });
      },
      (err) => {
        this.isLoading    = false;
        this.errorMessage = err.error?.errorMessage || 'Login failed. Please try again.';
        setTimeout(() => { this.errorMessage = ''; }, 3000);
      }
    );
  }
}

///////////////////////////////////////////////////////////////
