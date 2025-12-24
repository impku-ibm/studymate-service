CREATE TABLE academic_years (
                               id BIGSERIAL PRIMARY KEY,
                               school_id VARCHAR(50) NOT NULL,
                               year_label VARCHAR(20) NOT NULL,   -- 2024-25
                               status VARCHAR(20) NOT NULL,       -- ACTIVE / COMPLETED
                               start_date DATE,
                               end_date DATE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT uq_school_year UNIQUE (school_id, year_label)
);
