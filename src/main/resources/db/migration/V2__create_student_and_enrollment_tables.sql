/* ============================================================
   STUDENT DIRECTORY
   Defines WHO the student is
   ============================================================ */

CREATE TABLE student (
                        id BIGSERIAL PRIMARY KEY,

                        user_id VARCHAR(100) NOT NULL UNIQUE,

                        school_id BIGINT NOT NULL,

                        admission_number VARCHAR(50) NOT NULL,

                        full_name VARCHAR(150) NOT NULL,

                        date_of_birth DATE,

                        admission_date DATE NOT NULL,

                        parent_name VARCHAR(150),

                        parent_mobile VARCHAR(20),

                        address TEXT,

                        status VARCHAR(20) NOT NULL,

                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT uq_student_school_admission
                           UNIQUE (school_id, admission_number),

                        CONSTRAINT fk_student_school
                           FOREIGN KEY (school_id)
                              REFERENCES school(id)
                              ON DELETE RESTRICT
);

/* ============================================================
   STUDENT ENROLLMENT
   Defines WHERE the student studies per academic year
   ============================================================ */

CREATE TABLE student_enrollment (
                                   id BIGSERIAL PRIMARY KEY,

                                   student_id BIGINT NOT NULL,

                                   academic_year_id BIGINT NOT NULL,

                                   class_id BIGINT NOT NULL,

                                   section_id BIGINT NOT NULL,

                                   roll_number INTEGER NOT NULL,

                                   status VARCHAR(20) NOT NULL,

                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

   /* One student can be enrolled only once per academic year */
                                   CONSTRAINT uq_student_year
                                      UNIQUE (student_id, academic_year_id),

   /* Roll number must be unique within a section for a year */
                                   CONSTRAINT uq_section_roll
                                      UNIQUE (academic_year_id, section_id, roll_number),

                                   CONSTRAINT fk_enrollment_student
                                      FOREIGN KEY (student_id)
                                         REFERENCES student(id)
                                         ON DELETE CASCADE,

                                   CONSTRAINT fk_enrollment_academic_year
                                      FOREIGN KEY (academic_year_id)
                                         REFERENCES academic_year(id)
                                         ON DELETE RESTRICT,

                                   CONSTRAINT fk_enrollment_class
                                      FOREIGN KEY (class_id)
                                         REFERENCES school_class(id)
                                         ON DELETE RESTRICT,

                                   CONSTRAINT fk_enrollment_section
                                      FOREIGN KEY (section_id)
                                         REFERENCES class_section_template(id)
                                         ON DELETE RESTRICT
);

/* ============================================================
   INDEXES FOR PERFORMANCE
   ============================================================ */

CREATE INDEX idx_student_school
   ON student (school_id);

CREATE INDEX idx_enrollment_year
   ON student_enrollment (academic_year_id);

CREATE INDEX idx_enrollment_section
   ON student_enrollment (section_id);

CREATE INDEX idx_enrollment_student
   ON student_enrollment (student_id);
