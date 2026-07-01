package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.ForgetPasswordRequestDTO;
import com.infy.visitormanagement.dto.LoginRequestDTO;
import com.infy.visitormanagement.dto.ResetPasswordDTO;
import com.infy.visitormanagement.entity.User;
import com.infy.visitormanagement.exception.VisitorManagementException;

public interface AuthService {
    User login(LoginRequestDTO login) throws VisitorManagementException;

    String verifyUser(ForgetPasswordRequestDTO request) throws VisitorManagementException;

    String resetPassword(ResetPasswordDTO request) throws VisitorManagementException;
}
 