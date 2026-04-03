-- Fee discounts per student
CREATE TABLE fee_discount (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    academic_year_id BIGINT NOT NULL,
    discount_type VARCHAR(30) NOT NULL,
    percentage DECIMAL(5,2),
    fixed_amount DECIMAL(10,2),
    reason TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fee_discount_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT fk_fee_discount_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT fk_fee_discount_year FOREIGN KEY (academic_year_id) REFERENCES academic_year(id),
    CONSTRAINT chk_discount_value CHECK (percentage IS NOT NULL OR fixed_amount IS NOT NULL),
    CONSTRAINT chk_discount_percentage CHECK (percentage IS NULL OR (percentage > 0 AND percentage <= 100)),
    CONSTRAINT chk_discount_amount CHECK (fixed_amount IS NULL OR fixed_amount > 0)
);

CREATE INDEX idx_fee_discount_student ON fee_discount(student_id, academic_year_id);
