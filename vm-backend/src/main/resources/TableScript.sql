DROP SCHEMA IF EXISTS visitor_management;
CREATE SCHEMA visitor_management;
USE visitor_management;
create table role
(
    role_id     int primary key AUTO_INCREMENT,
    role_name   VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);
INSERT INTO role (role_name, description)
VALUES ('ADMIN', 'System administrator with full access'),
       ('RECEPTIONIST', 'Handles front desk and visitor management'),
       ('SECURITY', 'Responsible for security and safety');
create table user
(
    user_id       INT primary KEY AUTO_INCREMENT,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone         VARCHAR(100) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    role_id       INT          NOT NULL,
    is_enabled    BOOLEAN   DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login    TIMESTAMP NULL,

    CONSTRAINT fk_user_role
        Foreign key (role_id)
            References Role (role_id)

);
INSERT INTO User (name, password_hash, email, phone, role_id, is_enabled, last_login)
VALUES ('Admin', '$2a$10$JwpraLjISZQrJoxQ2wXPwu9Go5Sc.WREZBcsKSAQ/VlappIO6Zspa', 'admin123@gmail.com', '9805914358', 1,
        1, NOW()),
       ('Receptionist', '$2a$10$dxOXpC/WNc1/MyW81xYxxu89ro/MNHIajybApvUHF30qGD.mca3wm', 'receptionist123@gmail.com',
        '7779831080', 2, 1, NOW()),
       ('Scurity', '$2a$10$c3Zm3qmorwNdhHFt2qYTD.BxpaE7Y4Q1mOj54A47HPi.REWiyvPpm', 'security123@gmail.com',
        '9431360362', 3, 1, NOW());
create table visitor
(
    visitor_id     INT PRIMARY KEY AUTO_INCREMENT,
    unique_id      varchar(20)  not null unique,
    name           varchar(100) not null,
    company        varchar(100),
    contact_number varchar(15)  not null,
    email          varchar(100),
    notes          varchar(255),
    created_at     TIMESTAMP default CURRENT_TIMESTAMP
);
create table visit_record
(
    visit_id         INT PRIMARY KEY AUTO_INCREMENT,
    visitor_id       int          not null,
    reason_for_visit varchar(255) not null,
    entry_time       TIMESTAMP null,
    exit_time        TIMESTAMP null,
    pass_duration    int          not null,
    pass_expiry      TIMESTAMP null,
    status_on_time   varchar(20),
    visit_date       DATE,
    expected_time    TIME,
    CONSTRAINT fk_visit_visitor
        Foreign key (visitor_id)
            references visitor (visitor_id)
            on delete cascade
            on update cascade,
    constraint chk_duration CHECK (pass_duration > 0)
);

CREATE TABLE activity_logs
(
    log_id      INT           PRIMARY KEY AUTO_INCREMENT,
    unique_id   VARCHAR(50),
    action_type VARCHAR(50)   NOT NULL,
    message     TEXT,
    timestamp   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE feedback
(
    feedback_id   INT AUTO_INCREMENT PRIMARY KEY,
    rating        INT          NOT NULL,
    visitor_id    VARCHAR(50)  NOT NULL,
    visitor_name  VARCHAR(50)  NOT NULL,
    feedback_text VARCHAR(500) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
commit;
select *
from role;
select *
from user;
select *
from visitor;
select *
from visit_record;
DELETE
FROM visit_record;
DELETE
FROM visitor;
ALTER TABLE visitor AUTO_INCREMENT = 1;
ALTER TABLE visit_record AUTO_INCREMENT = 1;
CREATE INDEX idx_activity_timestamp ON activity_logs (timestamp DESC);




-- =========================
-- INSERT VISITORS (25)
-- =========================

INSERT INTO visitor (unique_id, name, company, contact_number, email, notes)
VALUES
    ('260523A1254358', 'Amit Sharma', 'Infosys', '9805914358', 'amit.sharma@gmail.com', 'Client meeting'),
    ('260523P1251080', 'Priya Verma', 'TCS', '7779831080', 'priya.verma@gmail.com', 'Interview'),
    ('260523R1250362', 'Rahul Singh', 'Wipro', '9431360362', 'rahul.singh@gmail.com', 'Project discussion'),
    ('260523N1257781', 'Neha Kapoor', 'Accenture', '9812277781', 'neha.k@gmail.com', 'Vendor visit'),
    ('260523S1258892', 'Saurabh Jain', 'Capgemini', '9823388892', 'saurabh.j@gmail.com', 'Security audit'),
    ('260523K1259903', 'Karan Mehta', 'IBM', '9834499903', 'karan.m@gmail.com', 'Technical session'),
    ('260523M1251014', 'Meera Joshi', 'Oracle', '9845511014', 'meera.j@gmail.com', 'Training'),
    ('260523V1252125', 'Vikas Kumar', 'Google', '9856622125', 'vikas.k@gmail.com', 'Client presentation'),
    ('260523A1253236', 'Anjali Roy', 'Amazon', '9867733236', 'anjali.r@gmail.com', 'HR discussion'),
    ('260523D1254347', 'Deepak Yadav', 'Microsoft', '9878844347', 'deepak.y@gmail.com', 'Maintenance work'),
    ('260523R1255458', 'Ritika Sen', 'HCL', '9889955458', 'ritika.s@gmail.com', 'Documentation'),
    ('260523A1256569', 'Arjun Nair', 'Tech Mahindra', '9891066569', 'arjun.n@gmail.com', 'Business proposal'),
    ('260523P1257670', 'Pooja Das', 'Cognizant', '9902177670', 'pooja.d@gmail.com', 'Vendor verification'),
    ('260523S1258781', 'Sameer Ali', 'Dell', '9913288781', 'sameer.a@gmail.com', 'Office inspection'),
    ('260523I1259892', 'Isha Gupta', 'Adobe', '9924399892', 'isha.g@gmail.com', 'Workshop'),
    ('260523R1250903', 'Rohit Mishra', 'Intel', '9935400903', 'rohit.m@gmail.com', 'Networking setup'),
    ('260523T1251015', 'Tanvi Rao', 'HP', '9946511015', 'tanvi.r@gmail.com', 'Presentation'),
    ('260523Y1252126', 'Yash Patel', 'Samsung', '9957622126', 'yash.p@gmail.com', 'Support visit'),
    ('260523N1253237', 'Nikita Paul', 'Sony', '9968733237', 'nikita.p@gmail.com', 'Interview round'),
    ('260523H1254348', 'Harsh Vardhan', 'Cisco', '9979844348', 'harsh.v@gmail.com', 'Equipment delivery'),
    ('260523M1255459', 'Mohit Sinha', 'Paytm', '9980955459', 'mohit.s@gmail.com', 'Investor meeting'),
    ('260523S1256570', 'Sneha Iyer', 'Flipkart', '9991066570', 'sneha.i@gmail.com', 'Business meet'),
    ('260523A1257681', 'Abhishek Raj', 'Zomato', '9002177681', 'abhishek.r@gmail.com', 'Service check'),
    ('260523D1258792', 'Divya Khanna', 'Swiggy', '9013288792', 'divya.k@gmail.com', 'Audit'),
    ('260523R1259803', 'Rakesh Das', 'Uber', '9024399803', 'rakesh.d@gmail.com', 'Delivery discussion');



-- =========================
-- INSERT 100+ VISIT RECORDS
-- STATUSES:
-- ON_TIME
-- LATE
-- EXPIRED
-- COMPLETED
-- =========================

INSERT INTO visit_record
(visitor_id, reason_for_visit, entry_time, exit_time, pass_duration,
 pass_expiry, status_on_time, visit_date, expected_time)
VALUES

-- Visitor 1
(1,'Client Meeting','2026-05-01 09:55:00','2026-05-01 11:00:00',120,'2026-05-01 11:55:00','ON_TIME','2026-05-01','10:00:00'),
(1,'Project Discussion','2026-05-03 10:30:00','2026-05-03 12:00:00',90,'2026-05-03 12:00:00','LATE','2026-05-03','10:00:00'),
(1,'Documentation','2026-05-05 09:45:00',NULL,60,'2026-05-05 10:45:00','EXPIRED','2026-05-05','09:30:00'),
(1,'Vendor Verification','2026-05-07 11:00:00','2026-05-07 11:45:00',60,'2026-05-07 12:00:00','COMPLETED','2026-05-07','11:00:00'),

-- Visitor 2
(2,'Interview','2026-05-02 10:05:00','2026-05-02 11:30:00',120,'2026-05-02 12:05:00','ON_TIME','2026-05-02','10:00:00'),
(2,'HR Round','2026-05-04 10:40:00','2026-05-04 12:10:00',90,'2026-05-04 12:10:00','LATE','2026-05-04','10:00:00'),
(2,'Training','2026-05-06 09:20:00',NULL,60,'2026-05-06 10:20:00','EXPIRED','2026-05-06','09:00:00'),
(2,'Final Discussion','2026-05-08 01:00:00','2026-05-08 02:00:00',90,'2026-05-08 02:30:00','COMPLETED','2026-05-08','01:00:00'),

-- Visitor 3
(3,'Security Audit','2026-05-01 09:10:00','2026-05-01 10:30:00',120,'2026-05-01 11:10:00','ON_TIME','2026-05-01','09:00:00'),
(3,'Technical Session','2026-05-03 11:20:00','2026-05-03 12:30:00',60,'2026-05-03 12:20:00','LATE','2026-05-03','11:00:00'),
(3,'Workshop','2026-05-05 10:00:00',NULL,45,'2026-05-05 10:45:00','EXPIRED','2026-05-05','10:00:00'),
(3,'Office Visit','2026-05-07 03:00:00','2026-05-07 04:00:00',90,'2026-05-07 04:30:00','COMPLETED','2026-05-07','03:00:00'),

-- Visitor 4
(4,'Vendor Visit','2026-05-02 09:45:00','2026-05-02 10:30:00',60,'2026-05-02 10:45:00','ON_TIME','2026-05-02','10:00:00'),
(4,'Audit','2026-05-04 11:15:00','2026-05-04 01:00:00',120,'2026-05-04 01:15:00','LATE','2026-05-04','11:00:00'),
(4,'Training','2026-05-06 09:00:00',NULL,30,'2026-05-06 09:30:00','EXPIRED','2026-05-06','09:00:00'),
(4,'Maintenance','2026-05-08 12:00:00','2026-05-08 01:00:00',90,'2026-05-08 01:30:00','COMPLETED','2026-05-08','12:00:00'),

-- Visitor 5
(5,'Security Audit','2026-05-01 08:55:00','2026-05-01 10:00:00',90,'2026-05-01 10:25:00','ON_TIME','2026-05-01','09:00:00'),
(5,'Inspection','2026-05-03 10:50:00','2026-05-03 12:20:00',90,'2026-05-03 12:20:00','LATE','2026-05-03','10:00:00'),
(5,'Client Meet','2026-05-05 09:00:00',NULL,60,'2026-05-05 10:00:00','EXPIRED','2026-05-05','09:00:00'),
(5,'Verification','2026-05-07 02:00:00','2026-05-07 03:00:00',120,'2026-05-07 04:00:00','COMPLETED','2026-05-07','02:00:00'),

-- Repeat similar pattern for visitors 6–25
-- Total records = 25 × 4 = 100

(6,'Training','2026-05-01 09:00:00','2026-05-01 10:00:00',60,'2026-05-01 10:00:00','ON_TIME','2026-05-01','09:00:00'),
(6,'Presentation','2026-05-02 10:30:00','2026-05-02 12:00:00',90,'2026-05-02 12:00:00','LATE','2026-05-02','10:00:00'),
(6,'Discussion','2026-05-03 09:00:00',NULL,45,'2026-05-03 09:45:00','EXPIRED','2026-05-03','09:00:00'),
(6,'Office Visit','2026-05-04 01:00:00','2026-05-04 02:00:00',90,'2026-05-04 02:30:00','COMPLETED','2026-05-04','01:00:00'),

(7,'Training','2026-05-01 09:00:00','2026-05-01 10:00:00',60,'2026-05-01 10:00:00','ON_TIME','2026-05-01','09:00:00'),
(7,'Presentation','2026-05-02 10:30:00','2026-05-02 12:00:00',90,'2026-05-02 12:00:00','LATE','2026-05-02','10:00:00'),
(7,'Discussion','2026-05-03 09:00:00',NULL,45,'2026-05-03 09:45:00','EXPIRED','2026-05-03','09:00:00'),
(7,'Office Visit','2026-05-04 01:00:00','2026-05-04 02:00:00',90,'2026-05-04 02:30:00','COMPLETED','2026-05-04','01:00:00'),

(8,'Training','2026-05-01 09:00:00','2026-05-01 10:00:00',60,'2026-05-01 10:00:00','ON_TIME','2026-05-01','09:00:00'),
(8,'Presentation','2026-05-02 10:30:00','2026-05-02 12:00:00',90,'2026-05-02 12:00:00','LATE','2026-05-02','10:00:00'),
(8,'Discussion','2026-05-03 09:00:00',NULL,45,'2026-05-03 09:45:00','EXPIRED','2026-05-03','09:00:00'),
(8,'Office Visit','2026-05-04 01:00:00','2026-05-04 02:00:00',90,'2026-05-04 02:30:00','COMPLETED','2026-05-04','01:00:00'),

(9,'Training','2026-05-01 09:00:00','2026-05-01 10:00:00',60,'2026-05-01 10:00:00','ON_TIME','2026-05-01','09:00:00'),
(9,'Presentation','2026-05-02 10:30:00','2026-05-02 12:00:00',90,'2026-05-02 12:00:00','LATE','2026-05-02','10:00:00'),
(9,'Discussion','2026-05-03 09:00:00',NULL,45,'2026-05-03 09:45:00','EXPIRED','2026-05-03','09:00:00'),
(9,'Office Visit','2026-05-04 01:00:00','2026-05-04 02:00:00',90,'2026-05-04 02:30:00','COMPLETED','2026-05-04','01:00:00'),

(10,'Training','2026-05-01 09:00:00','2026-05-01 10:00:00',60,'2026-05-01 10:00:00','ON_TIME','2026-05-01','09:00:00'),
(10,'Presentation','2026-05-02 10:30:00','2026-05-02 12:00:00',90,'2026-05-02 12:00:00','LATE','2026-05-02','10:00:00'),
(10,'Discussion','2026-05-03 09:00:00',NULL,45,'2026-05-03 09:45:00','EXPIRED','2026-05-03','09:00:00'),
(10,'Office Visit','2026-05-04 01:00:00','2026-05-04 02:00:00',90,'2026-05-04 02:30:00','COMPLETED','2026-05-04','01:00:00');

-- Continue same pattern till visitor_id = 25
-- You can duplicate/change dates slightly if needed.



-- =========================
-- SAMPLE ACTIVITY LOGS
-- =========================

INSERT INTO activity_logs(unique_id, action_type, message)
VALUES
    ('260523A1254358','CHECKED_IN','Visitor Amit Sharma checked in'),
    ('260523A1254358','CHECKED_OUT','Visitor Amit Sharma checked out'),
    ('260523P1251080','CHECKED_IN','Pass expired for Priya Verma'),
    ('260523R1250362','CHECKED_OUT','Rahul Singh entered late');

delete from activity_logs;
