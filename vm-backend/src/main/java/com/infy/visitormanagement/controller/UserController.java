package com.infy.visitormanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.infy.visitormanagement.dto.UserDTO;
import com.infy.visitormanagement.dto.UserResponseDTO;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private Environment environment;

    @PostMapping("/user/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) throws VisitorManagementException {
        UserResponseDTO userResponseDTO = userService.registerUser(userDTO);
        return new ResponseEntity<UserResponseDTO>(userResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("user")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) throws VisitorManagementException {
        Page<UserResponseDTO> users = userService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id) throws VisitorManagementException {
        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        String message = environment.getProperty("User.DELETE.SUCCESS");
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/disable/{id}")
    public ResponseEntity<Map<String, String>> disableUser(@PathVariable Integer id) throws VisitorManagementException {
        userService.disableUser(id);
        Map<String, String> response = new HashMap<>();
        String message = environment.getProperty("User.DISABLE.SUCCESS");
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/enable/{id}")
    public ResponseEntity<Map<String, String>> EnableUser(@PathVariable Integer id) throws VisitorManagementException {
        userService.enableUser(id);
        Map<String, String> response = new HashMap<>();
        String message = environment.getProperty("User.ENABLE.SUCCESS");
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}
