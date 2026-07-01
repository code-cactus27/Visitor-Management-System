import { User } from "./user";
export interface UserResponse {
  content:User[];
  totalElements:number;
  totalPages:number;
}
