-- V6__Create_Accounts_Tables.sql

-- Fee Structure Table
CREATE TABLE fee_structure (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    academic_year_id BIGINT NOT NULL,
    class_id BIGINT NOT NULL,
    fee_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    due_date DATE NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES school(id),
    FOREIGN KEY (academic_year_id) REFERENCES academic_year(id),
    FOREIGN KEY (class_id) REFERENCES school_class(id),
    CONSTRAINT uk_fee_structure UNIQUE (school_id, academic_year_id, class_id, fee_type)
);

-- Student Fee Table
CREATE TABLE student_fee (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    fee_structure_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) NOT NULL,
    paid_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    due_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (fee_structure_id) REFERENCES fee_structure(id),
    CONSTRAINT uk_student_fee UNIQUE (student_id, fee_structure_id)
);

-- Payment Table
CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    payment_mode VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    transaction_reference VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    FOREIGN KEY (student_id) REFERENCES student(id)
);

-- Payment Detail Table
CREATE TABLE payment_detail (
    id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL,
    student_fee_id BIGINT NOT NULL,
    amount_paid DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (payment_id) REFERENCES payment(id),
    FOREIGN KEY (student_fee_id) REFERENCES student_fee(id)
);

-- Fee Audit Log Table
CREATE TABLE fee_audit_log (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    details TEXT,
    performed_by VARCHAR(100),
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES school(id)
);

-- Indexes for performance
CREATE INDEX idx_student_fee_student ON student_fee(student_id);
CREATE INDEX idx_student_fee_status ON student_fee(status);
CREATE INDEX idx_payment_date ON payment(payment_date);
CREATE INDEX idx_payment_student ON payment(student_id);
CREATE INDEX idx_audit_school_date ON fee_audit_log(school_id, performed_at);