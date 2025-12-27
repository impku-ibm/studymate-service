CREATE TABLE teachers (
                         id BIGSERIAL PRIMARY KEY,
                         teacher_code VARCHAR(20) NOT NULL,
                         school_id VARCHAR(50) NOT NULL,

                         full_name VARCHAR(100) NOT NULL,
                         email VARCHAR(100) NOT NULL,
                         mobile_number VARCHAR(20) NOT NULL,
                         qualification VARCHAR(100) NOT NULL,

                         notes VARCHAR(500),
                         status VARCHAR(20) NOT NULL,

                         deleted BOOLEAN DEFAULT FALSE,

                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL,

                         CONSTRAINT uq_teacher_school_email UNIQUE (school_id, email)
);
