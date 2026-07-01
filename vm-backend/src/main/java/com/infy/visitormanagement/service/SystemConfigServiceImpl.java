package com.infy.visitormanagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.infy.visitormanagement.entity.SystemConfig;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.repository.SystemConfigRepository;

@Service
public class SystemConfigServiceImpl {
    @Autowired
    private SystemConfigRepository repository;

    // Get all configs as key-value map
    public Map<String, String> getAllConfigs() throws VisitorManagementException {
        List<SystemConfig> list = repository.findAll();
        Map<String, String> map = new HashMap<>();
        for (SystemConfig config : list) {
            map.put(config.getConfigKey(), config.getConfigValue());
        }
        return map;
    }

    // Update multiple configs
    public void updateConfigs(Map<String, String> updates) throws VisitorManagementException {
        for (Map.Entry<String, String> entry : updates.entrySet()) {
            SystemConfig config = repository.findById(entry.getKey())
                    .orElseThrow(() -> new VisitorManagementException("Service.CONFIG_NOT_FOUND"));
            config.setConfigValue(entry.getValue());
            repository.save(config);
        }
    }
}
