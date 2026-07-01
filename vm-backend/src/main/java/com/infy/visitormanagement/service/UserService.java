package com.infy.visitormanagement.service;

import org.springframework.data.domain.Page;
import com.infy.visitormanagement.dto.UserDTO;
import com.infy.visitormanagement.dto.UserResponseDTO;
import com.infy.visitormanagement.exception.VisitorManagementException;

public interface UserService {
    UserResponseDTO registerUser(UserDTO dto) throws VisitorManagementException;

    Page<UserResponseDTO> getAllUsers(int page, int size) throws VisitorManagementException;

    void deleteUser(Integer id) throws VisitorManagementException;

    void disableUser(Integer id) throws VisitorManagementException;

    void enableUser(Integer id) throws VisitorManagementException;
}
