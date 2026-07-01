package com.infy.visitormanagement.repository;
import com.infy.visitormanagement.entity.ActivityLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ActivityLogsRepository extends JpaRepository<ActivityLogs, Long> {
    List<ActivityLogs> findAllByOrderByTimestampDesc();
    List<ActivityLogs> findByUniqueIdOrderByTimestampDesc(String uniqueId);
}