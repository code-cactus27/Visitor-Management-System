package com.infy.visitormanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.infy.visitormanagement.dto.VisitorReportDTO;
import com.infy.visitormanagement.entity.VisitRecord;

public interface VisitRecordRepository extends JpaRepository<VisitRecord, Integer> {
    List<VisitRecord> findByVisitor_UniqueIdOrderByVisitDateDescExpectedTimeDesc(String uniqueId);

    @Query("SELECT COUNT(v) FROM VisitRecord v")
    int countTotalVisitors();

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE v.visitDate=CURRENT_DATE")
    int countTodayVisitors();

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE v.statusOnTime='CHECKED_IN'")
    int countCheckedIn();

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE v.statusOnTime='CHECKED_OUT'")
    int countCheckedOut();

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE v.statusOnTime='PENDING'")
    int countPending();

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE v.statusOnTime='EXPIRED'")
    int countExpired();

    @Query(value = """
            SELECT DATE(entry_time),COUNT(*)
            FROM visit_record
            WHERE entry_time BETWEEN:start AND :end
            GROUP BY DATE(entry_time)
            ORDER BY DATE(entry_time)
            """, nativeQuery = true)
    List<Object[]> getDaily(LocalDateTime start, LocalDateTime end);

    @Query(value = """
            SELECT YEARWEEK(entry_time),COUNT(*)
            FROM visit_record
            WHERE entry_time BETWEEN:start AND :end
            GROUP BY YEARWEEK(entry_time)
            ORDER BY YEARWEEK(entry_time)
            """, nativeQuery = true)
    List<Object[]> getWeekly(LocalDateTime start, LocalDateTime end);

    @Query(value = """
            SELECT DATE_FORMAT(entry_time,'%Y-%m'),COUNT(*)
            FROM visit_record
            WHERE entry_time BETWEEN:start AND :end
            GROUP BY DATE_FORMAT(entry_time,'%Y-%m')
            ORDER BY DATE_FORMAT(entry_time,'%Y-%m')
            """, nativeQuery = true)
    List<Object[]> getMonthly(LocalDateTime start, LocalDateTime end);

    @Query(value = """
            SELECT COUNT(*)
            FROM visit_record
            WHERE entry_time BETWEEN:start AND :end
            """, nativeQuery = true)
    Long getTotal(LocalDateTime start, LocalDateTime end);

    @Query(value = """
                        SELECT AVG(TIMESTAMPDIFF(SECOND,entry_time,exit_time))
                        FROM visit_record
                        WHERE entry_time IS NOT NULL
            AND exit_time IS NOT NULL
                        AND entry_time BETWEEN:start AND :end
            """, nativeQuery = true)
    Double getAvg(LocalDateTime start, LocalDateTime end);

    @Query(value = """
                            SELECT new com.infy.visitormanagement.dto.VisitorReportDTO(
                            v.name,
                            v.email,
                            v.contactNumber,
                            vr.entryTime,
                            vr.exitTime
            )
            FROM VisitRecord vr
            JOIN vr.visitor v
            WHERE vr.entryTime BETWEEN :start AND :end
            ORDER BY vr.entryTime ASC
            """)
    List<VisitorReportDTO> getReportData(LocalDateTime start, LocalDateTime end);
}