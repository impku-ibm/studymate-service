CREATE TABLE classes (
                        id BIGSERIAL PRIMARY KEY,
                        school_id VARCHAR(50) NOT NULL,
                        academic_year_id BIGINT NOT NULL,
                        class_name VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT uq_class UNIQUE (school_id, academic_year_id, class_name),
                        CONSTRAINT fk_class_academic_year
                           FOREIGN KEY (academic_year_id)
                              REFERENCES academic_years(id)
);
