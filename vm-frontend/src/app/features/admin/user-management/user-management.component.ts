import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { AdminService } from 'src/app/core/services/admin.service';
import { ThemeService } from 'src/app/core/services/theme.service';
import { User } from '../../../core/models/user';


function strongPasswordValidator(control: AbstractControl){
  const val: string = control.value || '';
  const hasUpper = /[A-Z]/.test(val);
  const hasLower = /[a-z]/.test(val);
  const hasDigit = /[0-9]/.test(val);
  const hasSpecial = /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>\/?]/.test(val);
  const hasMinLen = val.length >= 6;

  return hasUpper && hasLower && hasDigit && hasSpecial && hasMinLen ? null : { weakPassword: true };
}

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent {

  constructor(private adminService:AdminService,private fb:FormBuilder,public themeService:ThemeService){

  }

  errorMessage:string='';
  successMessage:string='';

  searchText:string='';
  filteredUsers:any[]=[];

  users:User[]=[];

  currentPage:number=0;
  totalPages:number=0;
  pageSize:number=10;
  userForm: any;
  showAddUserForm: boolean= false;



  ngOnInit(){
    this.loadUsers();
    this.initForm();
  }

  initForm(): void {
    this.userForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone:['',Validators.required],
      password: ['', [Validators.required, strongPasswordValidator]],
      role: ['', [Validators.required]]
    });
  }


//Open modal and reset form
  openAddUserForm(): void{
    this.showAddUserForm = true;
// this.userForm.reset();
  }

//Close modal
  closeAddUserForm(): void{
    this.showAddUserForm = false;
  }

  submit(){
    this.successMessage='';
    this.errorMessage='';
    const formValue=this.userForm.value;

    const payload={
      name:formValue.name,
      email:formValue.email,
      phone:formValue.phone,
      password:formValue.password,
      role:{
        roleName:formValue.role
      }
    };
    console.log(payload);

    this.adminService.addUser(payload).subscribe((res)=>{
      this.successMessage="User added sucessfully"
      console.log(this.successMessage);
      setTimeout(() => {
        this.successMessage='';
        this.loadUsers();
      },2000);
    },(err)=>{
      console.log(err);
      this.errorMessage=
        err.error?.errorMessage|| "Resgistration failed ";
      setTimeout(() => {
        this.errorMessage='';
        this.loadUsers();
      },2000);
    })

  }

  loadUsers(){
    this.adminService.getUsers(this.currentPage,this.pageSize).subscribe((res)=>{
        this.users=res.content;
        this.filteredUsers=res.content;
        this.totalPages=res.totalPages;



      },
      (error)=>{
        console.log(error);
      }
    )

  }


  searchUsers():void{
    const search=this.searchText? this.searchText.toLowerCase():'';

    this.filteredUsers=this.users.filter((user:any)=>{
      return (
        user.name.toLowerCase().indexOf(search)!==-1 ||
        user.email.toLowerCase().indexOf(search)!==-1 ||
        user.roleName.toLowerCase().indexOf(search)!==-1

      );
    });
  }

  disableUser(id:number){
    this.adminService.disableUser(id).subscribe({
        next:(response:any)=>{
          this.successMessage=response.message;
          this.errorMessage='';
          setTimeout(()=>{
            this.successMessage='';
            this.loadUsers();
          },1000)
        },

        error:(error)=>{
          this.errorMessage=error.error.message;
          this.successMessage='';

          setTimeout(()=>{
            this.errorMessage='';

          },2000)

        }
      }


    )
  }

  enableUser(id:number){
    this.adminService.enableUser(id).subscribe({
        next:(response:any)=>{
          this.successMessage=response.message;
          this.errorMessage='';
          setTimeout(()=>{
            this.successMessage='';
            this.loadUsers();
          },1000)
        },

        error:(error)=>{
          this.errorMessage=error.error.message;
          this.successMessage='';

          setTimeout(()=>{
            this.errorMessage='';
          },2000)

        }
      }


    )
  }

  deleteUser(id:number){

    if(confirm('Are you sure you want to delete this user?')){
      this.adminService.deleteUser(id).subscribe({
        next:(response)=>{
          this.successMessage=response.message;
          this.errorMessage='';
          setTimeout(()=>{
            this.successMessage='';
            this.users=this.users.filter((user:any)=>user.userId!==id);
            this.filteredUsers=this.users;
          },2000)
        },
        error:(error)=>{
          this.successMessage='';
          if(error.error.message){
            this.errorMessage=error.error.message;
          }
          else{
            this.errorMessage="Unable to delete user";
          }
          setTimeout(() => {
            this.errorMessage='';
          }, 2000);
        }
      })
    }
  }

  nextPage(){
    if(this.currentPage>0){
      this.currentPage++;
      this.loadUsers();
    }
  }

  prevPage(){
    if(this.currentPage>0){
      this.currentPage--;
      this.loadUsers();
    }
  }


}
