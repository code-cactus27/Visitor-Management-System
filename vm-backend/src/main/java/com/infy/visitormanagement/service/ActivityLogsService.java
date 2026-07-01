package com.infy.visitormanagement.service;
import com.infy.visitormanagement.dto.ActivityLogsDTO;
import com.infy.visitormanagement.entity.ActivityLogs;
import com.infy.visitormanagement.repository.ActivityLogsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
public class ActivityLogsService {
    @Autowired
    private ActivityLogsRepository activityLogsRepository;
    ModelMapper modelMapper = new ModelMapper();
    public List<ActivityLogsDTO> getActivityLogs() {
        List<ActivityLogs> logs = activityLogsRepository.findAllByOrderByTimestampDesc();
        List<ActivityLogsDTO> dtoList = new ArrayList<>();
        for (ActivityLogs log : logs) {
            ActivityLogsDTO dto = modelMapper.map(log, ActivityLogsDTO.class);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
