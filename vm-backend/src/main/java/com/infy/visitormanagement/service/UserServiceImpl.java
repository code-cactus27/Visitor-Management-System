package com.infy.visitormanagement.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import com.infy.visitormanagement.dto.UserDTO;
import com.infy.visitormanagement.dto.UserResponseDTO;
import com.infy.visitormanagement.entity.Role;
import com.infy.visitormanagement.entity.User;
import com.infy.visitormanagement.enums.RoleType;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.repository.RoleRepository;
import com.infy.visitormanagement.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserResponseDTO registerUser(UserDTO dto) throws VisitorManagementException {
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());

        if (userOptional.isPresent()) {
            throw new VisitorManagementException("Service.CANNOT_REGISTER_USER");
        }

        RoleType roleName = dto.getRole().getRoleName();

        Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);

        Role role = roleOptional.orElseThrow(() -> new VisitorManagementException("Service.ROLE_NOT_FOUND"));

        User user = modelMapper.map(dto, User.class);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(role);

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponseDTO.class);


    }

    @Override
    public Page<UserResponseDTO> getAllUsers(int page, int size) throws VisitorManagementException {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(user -> modelMapper.map(user, UserResponseDTO.class));
    }

    @Override
    public void deleteUser(Integer id) throws VisitorManagementException {
        Optional<User> userOptional = userRepository.findById(id);

        User user = userOptional.orElseThrow(() -> new VisitorManagementException("Service.USER_NOT_FOUND"));

        if (user.getRole().getRoleName() == RoleType.ADMIN) {
            throw new VisitorManagementException("Service.DELETE.ADMIN");
        }

        userRepository.delete(user);
    }

    @Override
    public void disableUser(Integer id) throws VisitorManagementException {
        Optional<User> userOptional = userRepository.findById(id);

        User user = userOptional.orElseThrow(() -> new VisitorManagementException("Service.USER_NOT_FOUND"));

        if (user.getRole().getRoleName() == RoleType.ADMIN) {
            throw new VisitorManagementException("Service.DISABLE.ADMIN");
        }

        user.setIsEnabled(false);
    }

    @Override
    public void enableUser(Integer id) throws VisitorManagementException {
        Optional<User> userOptional = userRepository.findById(id);

        User user = userOptional.orElseThrow(() -> new VisitorManagementException("Service.USER_NOT_FOUND"));


        user.setIsEnabled(true);
    }

}

