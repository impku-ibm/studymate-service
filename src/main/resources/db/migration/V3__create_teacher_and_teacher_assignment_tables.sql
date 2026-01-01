/* ============================================================
   TEACHER DIRECTORY
   Defines WHO the teacher is
   ============================================================ */

CREATE TABLE teacher (
                        id BIGSERIAL PRIMARY KEY,

                        user_id VARCHAR(100) NOT NULL UNIQUE,   -- Auth service user id

                        school_id BIGINT NOT NULL,

                        teacher_code VARCHAR(50),

                        full_name VARCHAR(150) NOT NULL,

                        email VARCHAR(150) NOT NULL,

                        phone VARCHAR(20),

                        qualification VARCHAR(150),

                        notes TEXT,

                        active BOOLEAN NOT NULL DEFAULT TRUE,

                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT uq_teacher_school_email
                           UNIQUE (school_id, email),

                        CONSTRAINT fk_teacher_school
                           FOREIGN KEY (school_id)
                              REFERENCES school(id)
                              ON DELETE RESTRICT
);

/* Indexes */
CREATE INDEX idx_teacher_school
   ON teacher (school_id);

CREATE INDEX idx_teacher_active
   ON teacher (active);


/* ============================================================
   TEACHER ASSIGNMENT
   Defines teaching responsibility per academic year
   ============================================================ */

CREATE TABLE teacher_assignment (
                                   id BIGSERIAL PRIMARY KEY,

                                   school_id BIGINT NOT NULL,

                                   academic_year_id BIGINT NOT NULL,

                                   teacher_id BIGINT NOT NULL,

                                   subject_id BIGINT NOT NULL,

                                   section_id BIGINT NOT NULL,

                                   active BOOLEAN NOT NULL DEFAULT TRUE,

                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

   /* One teacher per subject per section per academic year */
                                   CONSTRAINT uq_teacher_assignment
                                      UNIQUE (academic_year_id, section_id, subject_id),

                                   CONSTRAINT fk_ta_school
                                      FOREIGN KEY (school_id)
                                         REFERENCES school(id)
                                         ON DELETE RESTRICT,

                                   CONSTRAINT fk_ta_academic_year
                                      FOREIGN KEY (academic_year_id)
                                         REFERENCES academic_year(id)
                                         ON DELETE RESTRICT,

                                   CONSTRAINT fk_ta_teacher
                                      FOREIGN KEY (teacher_id)
                                         REFERENCES teacher(id)
                                         ON DELETE CASCADE,

                                   CONSTRAINT fk_ta_subject
                                      FOREIGN KEY (subject_id)
                                         REFERENCES subject(id)
                                         ON DELETE RESTRICT,

                                   CONSTRAINT fk_ta_section
                                      FOREIGN KEY (section_id)
                                         REFERENCES class_section_template(id)
                                         ON DELETE RESTRICT
);

/* Indexes for fast lookups */
CREATE INDEX idx_ta_teacher
   ON teacher_assignment (teacher_id);

CREATE INDEX idx_ta_section
   ON teacher_assignment (section_id);

CREATE INDEX idx_ta_year
   ON teacher_assignment (academic_year_id);
