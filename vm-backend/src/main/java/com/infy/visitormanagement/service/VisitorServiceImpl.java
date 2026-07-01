package com.infy.visitormanagement.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.infy.visitormanagement.entity.ActivityLogs;
import com.infy.visitormanagement.enums.LogAction;
import com.infy.visitormanagement.repository.ActivityLogsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.infy.visitormanagement.dto.VisitorRequestDTO;
import com.infy.visitormanagement.dto.VisitorResponseDTO;
import com.infy.visitormanagement.entity.Visitor;
import com.infy.visitormanagement.repository.VisitorRepository;
import jakarta.transaction.Transactional;

@Service("visitorService")
@Transactional
public class VisitorServiceImpl implements VisitorService {
    ModelMapper mm = new ModelMapper();
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private ActivityLogsRepository activityLogsRepository;

    @Override
    public String generateUniqueId(String name, String contactNumber) {
        String part1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String part2 = name.substring(0, 1).toUpperCase();
        String part3 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ss"));
        String part4 = contactNumber.substring(7, contactNumber.length());
        return part1 + part2 + part3 + part4;
    }

    @Override
    public List<VisitorResponseDTO> getAllVisitors() {
        List<VisitorResponseDTO> dtoList = new ArrayList<>();
        List<Visitor> visitorList = visitorRepository.findAllByOrderByCreatedAtDesc();
        for (Visitor d : visitorList) {
            VisitorResponseDTO oneDto = mm.map(d, VisitorResponseDTO.class);
            dtoList.add(oneDto);
        }
        return dtoList;
    }

    @Override
    public String addVisitor(VisitorRequestDTO visitorRequestDto) {
        Visitor vis = mm.map(visitorRequestDto, Visitor.class);
        vis.setUniqueId(generateUniqueId(visitorRequestDto.getName(), visitorRequestDto.getContactNumber()));
        vis.setCreatedAt(LocalDateTime.now());
        visitorRepository.save(vis);
        return vis.getUniqueId();
    }

    @Override
    public VisitorResponseDTO getOneVisitor(String uniqueId) {
        Visitor response = visitorRepository.getByUniqueId(uniqueId);
        VisitorResponseDTO responseDTO = mm.map(response, VisitorResponseDTO.class);
        return responseDTO;
    }

    @Override
    public String updateVisitor(String uniqueId, VisitorRequestDTO dto) {
        Visitor visitor = visitorRepository.getByUniqueId(uniqueId);
        if (visitor == null) {
            return "Not Found";
        }
        visitor.setName(dto.getName());
        visitor.setCompany(dto.getCompany());
        visitor.setContactNumber(dto.getContactNumber());
        visitor.setEmail(dto.getEmail());
        visitorRepository.save(visitor);
        logActivity(uniqueId, LogAction.VISITOR_EDITED, "Visitor updated");
        return "Updated succesfully with ID: " + uniqueId;
    }

    @Override
    public String deleteVisitor(String uniqueId) {
        Visitor visitor = visitorRepository.getByUniqueId(uniqueId);
        if (visitor == null) {
            return "Visitor Not Found";
        }
        visitorRepository.delete(visitor);
        logActivity(uniqueId, LogAction.VISITOR_DELETED, "Visitor deleted");
        return "Visitor Deleted Successfully";
    }

    private void logActivity(String uniqueId, LogAction action, String message) {
        ActivityLogs log = new ActivityLogs(
                uniqueId,
                action,
                message,
                LocalDateTime.now()
        );
        activityLogsRepository.save(log);
    }
}
