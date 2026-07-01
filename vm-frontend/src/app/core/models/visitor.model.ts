export interface VisitRecord{
  reasonForVisit: string;
}
export interface Visitor {
  name: string;
  company?:string;
  contactNumber:string;
  email:string;
  notes:string;
  visitRecord:VisitRecord[];
}
