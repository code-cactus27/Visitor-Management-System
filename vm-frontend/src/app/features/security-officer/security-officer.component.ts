import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemeService } from 'src/app/core/services/theme.service';
@Component({
  selector: 'app-security-officer',
  templateUrl: './security-officer.component.html',
  styleUrls: ['./security-officer.component.css']
})
export class SecurityOfficerComponent {
  searchForm!:FormGroup;
  activeView: string = 'home';
  onViewChange(view: string) { this.activeView = view; }
  onPremises=1;
  checkIns=3;
  pending=2;
  recentCheckins=[
    {name:'Emma Watson',time:'10:30 AM'},
    {name:'Robert Brown',time:'09:15 AM (expired)'}
  ];
  constructor(private fb:FormBuilder, public themeService: ThemeService){}
  ngOnInit():void{
    this.searchForm=this.fb.group({
      query:['',Validators.required]
    });
  }
  onSearch(){
    if(this.searchForm.invalid){
      this.searchForm.markAllAsTouched;
      return;
    }
    const value=this.searchForm.value.query;
    console.log("Searching For:",value)
  }
  visitors=[
    {name:'John Smith',id:'V202503250001',company:'Acme Inc',status:'Pending'},
    {name:'Emma Watson',id:'V202503250002',company:'Tech Corp',status:'Checked-In'},
    {name:'Robert Brown',id:'V202503250003',company:'XYZ Ltd',status:'Expired'}
  ];
  filteredVisitors=[...this.visitors];
  selectedVisitors:any=null;
  showDetails=false;
  searchPerformed=false;
  onSearch2(){
    this.searchPerformed=true;
    const query=this.searchForm.value.query?.trim().toLowerCase();
    if(!query){
      this.filteredVisitors=[...this.visitors];
      return;
    }
    this.filteredVisitors=this.visitors.filter(v=>
      v.name.toLowerCase()==(query)||
      v.id.toLowerCase()==(query)
    );
  }
  viewVisitor(visitor:any){
    this.selectedVisitors=visitor;
    this.showDetails=true;
  }
}
