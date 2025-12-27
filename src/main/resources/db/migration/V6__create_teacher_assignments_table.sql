CREATE TABLE teacher_assignments (
                                    id BIGSERIAL PRIMARY KEY,
                                    school_id VARCHAR(50) NOT NULL,

                                    teacher_id BIGINT NOT NULL,
                                    class_id BIGINT NOT NULL,
                                    section VARCHAR(10) NOT NULL,
                                    subject_id BIGINT NOT NULL,
                                    academic_year_id BIGINT NOT NULL,

                                    created_at TIMESTAMP NOT NULL,

                                    CONSTRAINT fk_teacher_assignment_teacher
                                       FOREIGN KEY (teacher_id) REFERENCES teachers(id),

                                    CONSTRAINT uq_teacher_assignment UNIQUE (
                                                                             teacher_id, class_id, section, subject_id, academic_year_id
                                       )
);
