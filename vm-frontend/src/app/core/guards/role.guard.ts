import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../services/auth.service";
import { catchError, map, of } from "rxjs";
export const roleGuard:CanActivateFn=(route)=>{
  const authService=inject(AuthService);
  const router=inject(Router);
  const requiredRole=route.data?.['role'] ||
    route.parent?.data?.['role'];
  return authService.getUser().pipe(
    map((user:any)=>{
      if(!user){
        return router.createUrlTree(['/login']);
      }
      if(user.role===requiredRole){
        return true;
      }
      return router.createUrlTree(['/login'])
    }),
    catchError((err)=>{
      return of(router.createUrlTree(['/login']));
    })
  );
};
