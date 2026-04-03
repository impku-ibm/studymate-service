-- Non-teaching staff
CREATE TABLE staff (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    user_id VARCHAR(255),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    staff_type VARCHAR(30) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_staff_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT uk_staff_email UNIQUE (school_id, email)
);

-- Staff attendance
CREATE TABLE staff_attendance (
    id BIGSERIAL PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_staff_att_staff FOREIGN KEY (staff_id) REFERENCES staff(id),
    CONSTRAINT uk_staff_attendance UNIQUE (staff_id, attendance_date)
);

CREATE INDEX idx_staff_school ON staff(school_id);
CREATE INDEX idx_staff_att_date ON staff_attendance(attendance_date);
