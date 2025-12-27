CREATE TABLE subject_master (
                               id BIGSERIAL PRIMARY KEY,
                               school_id VARCHAR(50) NOT NULL,
                               name VARCHAR(100) NOT NULL,
                               code VARCHAR(50),
                               category VARCHAR(30), -- CORE, OPTIONAL, LANGUAGE
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT uq_school_subject UNIQUE (school_id, name)
);

CREATE TABLE class_subject (
                              id BIGSERIAL PRIMARY KEY,
                              school_id VARCHAR(50) NOT NULL,
                              academic_year_id BIGINT NOT NULL,
                              class_id BIGINT NOT NULL,
                              section_id BIGINT,
                              subject_id BIGINT NOT NULL,
                              weekly_periods INT,
                              is_optional BOOLEAN DEFAULT false,

                              CONSTRAINT uq_class_subject UNIQUE (
                                                                  academic_year_id,
                                                                  class_id,
                                                                  section_id,
                                                                  subject_id
                                 )
);
