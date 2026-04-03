-- Create exams table
CREATE TABLE exams (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    academic_year_id BIGINT NOT NULL,
    exam_type VARCHAR(30) NOT NULL,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    publish_result BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_exam_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT fk_exam_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_year(id),
    CONSTRAINT uk_exam_type_year_school UNIQUE (school_id, academic_year_id, exam_type)
);

-- Create exam_schedules table
CREATE TABLE exam_schedules (
    id BIGSERIAL PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    school_class_id BIGINT NOT NULL,
    section VARCHAR(10) NOT NULL,
    subject_id BIGINT NOT NULL,
    exam_date DATE NOT NULL,
    max_marks INTEGER NOT NULL CHECK (max_marks > 0),
    pass_marks INTEGER NOT NULL CHECK (pass_marks >= 0),
    duration_minutes INTEGER NOT NULL CHECK (duration_minutes > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_exam_schedule_exam FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE,
    CONSTRAINT fk_exam_schedule_class FOREIGN KEY (school_class_id) REFERENCES school_class(id),
    CONSTRAINT fk_exam_schedule_subject FOREIGN KEY (subject_id) REFERENCES subject(id),
    CONSTRAINT uk_exam_schedule UNIQUE (exam_id, school_class_id, section, subject_id)
);

-- Create exam_marks table
CREATE TABLE exam_marks (
    id BIGSERIAL PRIMARY KEY,
    exam_schedule_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    marks_obtained INTEGER CHECK (marks_obtained >= 0),
    absent BOOLEAN DEFAULT FALSE,
    remarks TEXT,
    entered_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_exam_marks_schedule FOREIGN KEY (exam_schedule_id) REFERENCES exam_schedules(id) ON DELETE CASCADE,
    CONSTRAINT fk_exam_marks_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT fk_exam_marks_teacher FOREIGN KEY (entered_by) REFERENCES teacher(id),
    CONSTRAINT uk_exam_marks UNIQUE (exam_schedule_id, student_id)
);

-- Create student_results table
CREATE TABLE student_results (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    exam_id BIGINT NOT NULL,
    total_marks INTEGER NOT NULL DEFAULT 0,
    max_possible_marks INTEGER NOT NULL DEFAULT 0,
    percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    grade VARCHAR(5),
    rank_in_class INTEGER,
    result_status VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_student_result_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT fk_student_result_exam FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE,
    CONSTRAINT uk_student_result UNIQUE (student_id, exam_id)
);

-- Create student_attendance table
CREATE TABLE student_attendance (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    academic_year_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(10) NOT NULL,
    marked_by BIGINT NOT NULL,
    section VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_student_attendance_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT fk_student_attendance_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_year(id),
    CONSTRAINT fk_student_attendance_teacher FOREIGN KEY (marked_by) REFERENCES teacher(id),
    CONSTRAINT uk_student_attendance UNIQUE (student_id, attendance_date)
);

-- Create teacher_attendance table
CREATE TABLE teacher_attendance (
    id BIGSERIAL PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_teacher_attendance_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(id),
    CONSTRAINT uk_teacher_attendance UNIQUE (teacher_id, attendance_date)
);

-- Create indexes
CREATE INDEX idx_exams_school_academic_year ON exams(school_id, academic_year_id);
CREATE INDEX idx_exam_schedules_exam_class ON exam_schedules(exam_id, school_class_id);
CREATE INDEX idx_exam_marks_schedule_student ON exam_marks(exam_schedule_id, student_id);
CREATE INDEX idx_student_results_exam ON student_results(exam_id);
CREATE INDEX idx_student_attendance_date ON student_attendance(attendance_date);
CREATE INDEX idx_student_attendance_student_year ON student_attendance(student_id, academic_year_id);
CREATE INDEX idx_teacher_attendance_date ON teacher_attendance(attendance_date);
