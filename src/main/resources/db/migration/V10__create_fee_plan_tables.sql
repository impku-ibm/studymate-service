-- Fee plans (Hosteller, Transport, Day Scholar etc.)
CREATE TABLE fee_plan (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fee_plan_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT uk_fee_plan_name UNIQUE (school_id, name)
);

-- Items within a fee plan
CREATE TABLE fee_plan_item (
    id BIGSERIAL PRIMARY KEY,
    fee_plan_id BIGINT NOT NULL,
    fee_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    frequency VARCHAR(20) NOT NULL DEFAULT 'MONTHLY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fee_plan_item_plan FOREIGN KEY (fee_plan_id) REFERENCES fee_plan(id) ON DELETE CASCADE,
    CONSTRAINT uk_fee_plan_item UNIQUE (fee_plan_id, fee_type),
    CONSTRAINT chk_fee_plan_amount CHECK (amount >= 0)
);

-- Add fee_plan_id to student table
ALTER TABLE student ADD COLUMN fee_plan_id BIGINT;
ALTER TABLE student ADD CONSTRAINT fk_student_fee_plan FOREIGN KEY (fee_plan_id) REFERENCES fee_plan(id);

CREATE INDEX idx_fee_plan_school ON fee_plan(school_id);
CREATE INDEX idx_student_fee_plan ON student(fee_plan_id);
