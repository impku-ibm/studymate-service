CREATE TABLE sections (
                         id BIGSERIAL PRIMARY KEY,
                         class_id BIGINT NOT NULL,
                         name VARCHAR(5) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT uq_section UNIQUE (class_id, name),
                         CONSTRAINT fk_section_class
                            FOREIGN KEY (class_id) REFERENCES classes(id)
);
