-- Transfer certificates
CREATE TABLE transfer_certificate (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    tc_number VARCHAR(50) NOT NULL,
    leaving_date DATE NOT NULL,
    reason_for_leaving TEXT,
    conduct VARCHAR(50) DEFAULT 'GOOD',
    remarks TEXT,
    generated_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tc_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT fk_tc_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT uk_tc_number UNIQUE (school_id, tc_number),
    CONSTRAINT uk_tc_student UNIQUE (student_id)
);

CREATE INDEX idx_tc_school ON transfer_certificate(school_id);
