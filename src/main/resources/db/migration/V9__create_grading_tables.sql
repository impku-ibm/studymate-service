-- Grading scale per school (multi-board support)
CREATE TABLE grading_scale (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_grading_scale_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT uk_grading_scale_name UNIQUE (school_id, name)
);

-- Grade entries within a scale
CREATE TABLE grading_scale_entry (
    id BIGSERIAL PRIMARY KEY,
    grading_scale_id BIGINT NOT NULL,
    grade_name VARCHAR(10) NOT NULL,
    min_percentage DECIMAL(5,2) NOT NULL,
    max_percentage DECIMAL(5,2) NOT NULL,
    grade_point DECIMAL(3,1),
    description VARCHAR(100),

    CONSTRAINT fk_entry_grading_scale FOREIGN KEY (grading_scale_id) REFERENCES grading_scale(id) ON DELETE CASCADE,
    CONSTRAINT uk_grade_name_per_scale UNIQUE (grading_scale_id, grade_name),
    CONSTRAINT chk_percentage_range CHECK (min_percentage >= 0 AND max_percentage <= 100 AND min_percentage <= max_percentage)
);

CREATE INDEX idx_grading_scale_school ON grading_scale(school_id);
