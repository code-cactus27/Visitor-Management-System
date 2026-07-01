package com.infy.visitormanagement.controller;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.infy.visitormanagement.dto.ForgetPasswordRequestDTO;
import com.infy.visitormanagement.dto.LoginRequestDTO;
import com.infy.visitormanagement.dto.LoginResponseDTO;
import com.infy.visitormanagement.dto.ResetPasswordDTO;
import com.infy.visitormanagement.entity.User;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
         @Autowired
 private AuthService authService;
         @PostMapping("/login")
 public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request,HttpServletRequest httprequest) throws VisitorManagementException{
 User user=authService.login(request);
 httprequest.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT",SecurityContextHolder.getContext());
 LoginResponseDTO response=new  LoginResponseDTO();
 response.setEmail(user.getEmail());
 response.setRole(user.getRole().getRoleName());
 return ResponseEntity.ok(response);
 }
            @GetMapping("/me")
 public ResponseEntity<?> getCurrentUser(Authentication auth){
//String role=auth.getAuthorities().stream().findFirst().get().getAuthority();
 User u=(User)auth.getPrincipal();
 return ResponseEntity.ok(Map.of(
                "email",u.getEmail(),
                "role",u.getRole().getRoleName()
                ));
 }
         @PostMapping("/logout")
 public  ResponseEntity<?> logout(HttpServletRequest request){
 HttpSession session=request.getSession(false);
 if(session!=null) {
 session.invalidate();
 }
 return ResponseEntity.ok("Logged out successfully");
 }
         @PostMapping("/verify-user")
 public ResponseEntity<?> verifyUser(@RequestBody @Valid ForgetPasswordRequestDTO request) throws VisitorManagementException {
 String msg = authService.verifyUser(request);
  Map<String,String> response=new HashMap<>();
         response.put("message", msg);
         return ResponseEntity.ok(response);
 }
         @PostMapping("/reset-password")
 public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO request) throws VisitorManagementException {
 String msg = authService.resetPassword(request);
  Map<String,String> response=new HashMap<>();
         response.put("message", msg);
         return ResponseEntity.ok(response);
 }
}
