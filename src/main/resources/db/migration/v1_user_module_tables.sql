-- =========================
-- USER MODULE - INITIAL SCHEMA
-- =========================

CREATE TABLE schools (
                        id UUID PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        board VARCHAR(50) NOT NULL,
                        address TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE academic_years (
                               id UUID PRIMARY KEY,
                               school_id UUID NOT NULL,
                               year_label VARCHAR(20) NOT NULL,
                               active BOOLEAN DEFAULT true,
                               CONSTRAINT fk_ay_school
                                  FOREIGN KEY (school_id) REFERENCES schools(id)
);

CREATE TABLE class_rooms (
                            id UUID PRIMARY KEY,
                            school_id UUID NOT NULL,
                            academic_year_id UUID NOT NULL,
                            class_name VARCHAR(10) NOT NULL,
                            section VARCHAR(5) NOT NULL,
                            CONSTRAINT fk_class_school
                               FOREIGN KEY (school_id) REFERENCES schools(id),
                            CONSTRAINT fk_class_ay
                               FOREIGN KEY (academic_year_id) REFERENCES academic_years(id),
                            CONSTRAINT uq_class UNIQUE (academic_year_id, class_name, section)
);

CREATE TABLE subjects (
                         id UUID PRIMARY KEY,
                         school_id UUID NOT NULL,
                         name VARCHAR(100) NOT NULL,
                         code VARCHAR(50),
                         CONSTRAINT fk_subject_school
                            FOREIGN KEY (school_id) REFERENCES schools(id)
);

CREATE TABLE students (
                         id UUID PRIMARY KEY,
                         school_id UUID NOT NULL,
                         academic_year_id UUID NOT NULL,
                         class_room_id UUID NOT NULL,
                         roll_number INT,
                         admission_number VARCHAR(50),
                         name VARCHAR(255) NOT NULL,
                         dob DATE,
                         gender VARCHAR(10),
                         active BOOLEAN DEFAULT true,
                         CONSTRAINT fk_student_school
                            FOREIGN KEY (school_id) REFERENCES schools(id),
                         CONSTRAINT fk_student_ay
                            FOREIGN KEY (academic_year_id) REFERENCES academic_years(id),
                         CONSTRAINT fk_student_class
                            FOREIGN KEY (class_room_id) REFERENCES class_rooms(id),
                         CONSTRAINT uq_student_roll UNIQUE (academic_year_id, class_room_id, roll_number)
);

CREATE TABLE teachers (
                         id UUID PRIMARY KEY,
                         school_id UUID NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         qualification VARCHAR(255),
                         active BOOLEAN DEFAULT true,
                         CONSTRAINT fk_teacher_school
                            FOREIGN KEY (school_id) REFERENCES schools(id)
);

CREATE TABLE teacher_assignments (
                                    id UUID PRIMARY KEY,
                                    teacher_id UUID NOT NULL,
                                    class_room_id UUID NOT NULL,
                                    subject_id UUID NOT NULL,
                                    CONSTRAINT fk_ta_teacher
                                       FOREIGN KEY (teacher_id) REFERENCES teachers(id),
                                    CONSTRAINT fk_ta_class
                                       FOREIGN KEY (class_room_id) REFERENCES class_rooms(id),
                                    CONSTRAINT fk_ta_subject
                                       FOREIGN KEY (subject_id) REFERENCES subjects(id),
                                    CONSTRAINT uq_teacher_assignment
                                       UNIQUE (teacher_id, class_room_id, subject_id)
);

CREATE TABLE user_role_mappings (
                                   user_id UUID NOT NULL,
                                   school_id UUID NOT NULL,
                                   role VARCHAR(50) NOT NULL,
                                   PRIMARY KEY (user_id, school_id, role)
);
