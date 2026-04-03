-- V8__Create_Student_Fee_Configuration_Table.sql

/**
 * Creates student_fee_configuration table to track which fee types are applicable for each student.
 * 
 * Purpose:
 * - Allow flexible fee assignment per student
 * - Support opt-in/opt-out of fee types (transport, hostel, etc.)
 * - Track fee configuration history
 * 
 * Example data:
 * Student A: TUITION (active), TRANSPORT (active), EXAM (active)
 * Student B: TUITION (active), HOSTEL (active), TRANSPORT (inactive - opted out)
 */

CREATE TABLE student_fee_configuration (
    id BIGSERIAL PRIMARY KEY,
    
    -- Student for whom this fee configuration applies
    student_id BIGINT NOT NULL,
    
    -- Academic year for this configuration
    academic_year_id BIGINT NOT NULL,
    
    -- Type of fee (TUITION, TRANSPORT, HOSTEL, EXAM, MISC)
    fee_type VARCHAR(50) NOT NULL,
    
    -- Whether this fee is currently active
    -- Can be set to false if student opts out
    active BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- When this fee configuration started
    start_date DATE NOT NULL,
    
    -- When this fee configuration ended (null = ongoing)
    end_date DATE,
    
    -- Audit fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (academic_year_id) REFERENCES academic_year(id),
    
    -- Unique constraint: One configuration per student per academic year per fee type
    CONSTRAINT uk_student_fee_config UNIQUE (student_id, academic_year_id, fee_type)
);

-- Indexes for performance
CREATE INDEX idx_student_fee_config_student ON student_fee_configuration(student_id);
CREATE INDEX idx_student_fee_config_active ON student_fee_configuration(active);
CREATE INDEX idx_student_fee_config_academic_year ON student_fee_configuration(academic_year_id);

-- Comment on table
COMMENT ON TABLE student_fee_configuration IS 'Tracks which fee types are applicable for each student, allowing flexible fee assignment and opt-in/opt-out functionality';
