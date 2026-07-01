package com.infy.visitormanagement.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import jakarta.persistence.Table;

@Entity
@Table(name = "visitor")
@Data
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visitor_id")
    private Integer visitorId;
    @Column(name = "unique_id", nullable = false, unique = true, length = 20)
    private String uniqueId;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "company", length = 100)
    private String company;
    @Column(name = "contact_number", nullable = false, length = 15)
    private String contactNumber;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "notes", length = 255)
    private String notes;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitRecord> visitRecord = new ArrayList<>();
}