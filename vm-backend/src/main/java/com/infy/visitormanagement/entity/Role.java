package com.infy.visitormanagement.entity;

import java.util.ArrayList;
import java.util.List;

import com.infy.visitormanagement.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType roleName;
    private String description;
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
}
