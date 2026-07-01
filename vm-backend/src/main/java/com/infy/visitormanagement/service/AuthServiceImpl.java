package com.infy.visitormanagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.infy.visitormanagement.dto.ForgetPasswordRequestDTO;
import com.infy.visitormanagement.dto.ResetPasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.infy.visitormanagement.dto.LoginRequestDTO;
import com.infy.visitormanagement.entity.User;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service(value = "authService")
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(LoginRequestDTO login) throws VisitorManagementException {
        Optional<User> userOptional = userRepository.findByEmail(login.getEmail());
        User user = userOptional.orElseThrow(() -> new VisitorManagementException("Service.USER_NOT_FOUND"));
        if (Boolean.FALSE.equals(user.getIsEnabled())) {
            throw new VisitorManagementException("Service.USER_DISABLED");
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPasswordHash())) {
            throw new VisitorManagementException("Service.INVALID_PASSWORD");
        }

        user.setLastLogin(LocalDateTime.now());
        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, roles);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return user;
    }

    @Override
    public String verifyUser(ForgetPasswordRequestDTO request) throws VisitorManagementException {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        User user = userOptional.orElseThrow(() -> new VisitorManagementException("Service.USER_NOT_FOUND"));
        String phone = user.getPhone();
        String last4Digits = phone.substring(phone.length() - 4);
        if (!last4Digits.equals(request.getLast4Digits())) {
            throw new VisitorManagementException("Service.VERIFICATION_FAILED");
        }
        return "Verified Successfully";
    }

    @Override
    public String resetPassword(ResetPasswordDTO request) throws VisitorManagementException {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        User user = userOptional.orElseThrow(() -> new VisitorManagementException("Service.USER_NOT_FOUND"));
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        return "Password Reset Successfully";
    }
}
 