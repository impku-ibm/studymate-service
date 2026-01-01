CREATE TABLE school (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       board VARCHAR(50) NOT NULL,
                       address TEXT NOT NULL,
                       city VARCHAR(100) NOT NULL,
                       state VARCHAR(100) NOT NULL,
                       academic_start_month VARCHAR(20) NOT NULL,
                       school_code VARCHAR(50) NOT NULL,
                       active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT uq_school_code UNIQUE (school_code)
);

CREATE TABLE academic_year (
                              id BIGSERIAL PRIMARY KEY,
                              school_id BIGINT NOT NULL,
                              year VARCHAR(20) NOT NULL,         -- e.g. 2024-25
                              status VARCHAR(20) NOT NULL,       -- ACTIVE / COMPLETED
                              active BOOLEAN DEFAULT TRUE,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_academic_year_school
                                 FOREIGN KEY (school_id)
                                    REFERENCES school(id),

                              CONSTRAINT uq_school_year
                                 UNIQUE (school_id, year)
);

CREATE TABLE school_class (
                             id BIGSERIAL PRIMARY KEY,
                             school_id BIGINT NOT NULL,
                             name VARCHAR(50) NOT NULL,          -- Class 1, Class 10
                             active BOOLEAN DEFAULT TRUE,

                             CONSTRAINT fk_class_school
                                FOREIGN KEY (school_id)
                                   REFERENCES school(id),

                             CONSTRAINT uq_school_class
                                UNIQUE (school_id, name)
);

CREATE TABLE class_section_template (
                                       id BIGSERIAL PRIMARY KEY,
                                       school_class_id BIGINT NOT NULL,
                                       name VARCHAR(10) NOT NULL,          -- A / B / C
                                       active BOOLEAN DEFAULT TRUE,

                                       CONSTRAINT fk_section_class
                                          FOREIGN KEY (school_class_id)
                                             REFERENCES school_class(id),

                                       CONSTRAINT uq_class_section
                                          UNIQUE (school_class_id, name)
);

CREATE TABLE subject (
                        id BIGSERIAL PRIMARY KEY,
                        school_id BIGINT NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        code VARCHAR(20) NOT NULL,
                        active BOOLEAN DEFAULT TRUE,

                        CONSTRAINT fk_subject_school
                           FOREIGN KEY (school_id)
                              REFERENCES school(id),

                        CONSTRAINT uq_subject_code
                           UNIQUE (school_id, code)
);

CREATE INDEX idx_academic_year_school
   ON academic_year (school_id);

CREATE INDEX idx_school_class_school
   ON school_class (school_id);

CREATE INDEX idx_subject_school
   ON subject (school_id);
