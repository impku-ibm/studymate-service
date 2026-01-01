/* ============================================================
   CLASS SUBJECT
   Defines which subjects are taught in a class per academic year
   ============================================================ */

CREATE TABLE class_subject (
                              id BIGSERIAL PRIMARY KEY,

                              academic_year_id BIGINT NOT NULL,

                              school_class_id BIGINT NOT NULL,

                              subject_id BIGINT NOT NULL,

                              active BOOLEAN NOT NULL DEFAULT TRUE,

                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

   /* One subject per class per academic year */
                              CONSTRAINT uq_class_subject
                                 UNIQUE (academic_year_id, school_class_id, subject_id),

                              CONSTRAINT fk_cs_academic_year
                                 FOREIGN KEY (academic_year_id)
                                    REFERENCES academic_year(id)
                                    ON DELETE RESTRICT,

                              CONSTRAINT fk_cs_school_class
                                 FOREIGN KEY (school_class_id)
                                    REFERENCES school_class(id)
                                    ON DELETE RESTRICT,

                              CONSTRAINT fk_cs_subject
                                 FOREIGN KEY (subject_id)
                                    REFERENCES subject(id)
                                    ON DELETE RESTRICT
);

/* Indexes for performance */
CREATE INDEX idx_cs_academic_year
   ON class_subject (academic_year_id);

CREATE INDEX idx_cs_school_class
   ON class_subject (school_class_id);

CREATE INDEX idx_cs_subject
   ON class_subject (subject_id);
