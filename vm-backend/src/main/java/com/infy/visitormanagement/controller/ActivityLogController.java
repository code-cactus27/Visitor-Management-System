package com.infy.visitormanagement.controller;
import com.infy.visitormanagement.dto.ActivityLogsDTO;
import com.infy.visitormanagement.service.ActivityLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/gate/activity-logs")
public class ActivityLogController {
    @Autowired
    private ActivityLogsService activityLogService;
    @GetMapping
    public List<ActivityLogsDTO> getAllLogs() {
        return activityLogService.getActivityLogs();
    }
}