package com.infy.visitormanagement.controller;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.service.SystemConfigServiceImpl;
@RestController
@RequestMapping("/api/admin")
public class SystemConfigController {
                     @Autowired
    private SystemConfigServiceImpl service;
                       @GetMapping("/config")
                 public Map<String, String> getConfigs() throws VisitorManagementException {
                     return service.getAllConfigs();
                 }
                 @PutMapping("/config")
                 public String updateConfigs(@RequestBody Map<String, String> updates) throws VisitorManagementException {
                     service.updateConfigs(updates);
                     return "Config updated successfully";
                 }
}
