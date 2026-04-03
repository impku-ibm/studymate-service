-- Period definitions per school
CREATE TABLE period_definition (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    period_number INTEGER NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_break BOOLEAN DEFAULT FALSE,
    label VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_period_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT uk_period_number UNIQUE (school_id, period_number),
    CONSTRAINT chk_period_time CHECK (start_time < end_time)
);

-- Timetable entries
CREATE TABLE timetable_entry (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    period_definition_id BIGINT NOT NULL,
    class_id BIGINT NOT NULL,
    section VARCHAR(10) NOT NULL,
    subject_id BIGINT,
    teacher_id BIGINT,
    day_of_week INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tt_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT fk_tt_period FOREIGN KEY (period_definition_id) REFERENCES period_definition(id) ON DELETE CASCADE,
    CONSTRAINT fk_tt_class FOREIGN KEY (class_id) REFERENCES school_class(id),
    CONSTRAINT fk_tt_subject FOREIGN KEY (subject_id) REFERENCES subject(id),
    CONSTRAINT fk_tt_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(id),
    CONSTRAINT uk_tt_class_period_day UNIQUE (class_id, section, period_definition_id, day_of_week),
    CONSTRAINT chk_day_of_week CHECK (day_of_week BETWEEN 1 AND 7)
);

CREATE INDEX idx_tt_class_section ON timetable_entry(class_id, section);
CREATE INDEX idx_tt_teacher ON timetable_entry(teacher_id);
CREATE INDEX idx_tt_day ON timetable_entry(day_of_week);
