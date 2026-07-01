package com.infy.visitormanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "system_config")
public class SystemConfig {
    @Id
    private String configKey;
    private String configValue;
}