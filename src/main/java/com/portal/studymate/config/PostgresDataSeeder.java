package com.portal.studymate.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Seeds PostgreSQL with test data on startup (local profile only).
 * Uses INSERT ... ON CONFLICT DO NOTHING to be idempotent.
 */
@Component
@Profile("local")
@Order(2) // Run after Flyway migrations
@RequiredArgsConstructor
@Slf4j
public class PostgresDataSeeder implements CommandLineRunner {

    private final JdbcTemplate jdbc;

    @Override
    public void run(String... args) {
        if (hasData()) {
            log.info("PostgreSQL already has data, skipping seed");
            return;
        }

        log.info("Seeding PostgreSQL test data...");
        seedSchool();
        seedAcademicYears();
        seedClasses();
        seedSubjects();
        seedTeachers();
        seedStudents();
        seedGradingScale();
        seedFeePlans();
        seedStaff();
        seedPeriods();
        log.info("PostgreSQL seed complete!");
    }

    private boolean hasData() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM school", Integer.class);
        return count != null && count > 0;
    }

    private void seedSchool() {
        jdbc.update("INSERT INTO school (name, board, address, city, state, academic_start_month, school_code, active, created_at) " +
            "VALUES ('Sunrise Public School', 'CBSE', '45 Nehru Nagar, Sector 12', 'New Delhi', 'Delhi', 'APRIL', 'SPS001', true, NOW())");
        log.info("  Seeded: 1 school");
    }

    private void seedAcademicYears() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        jdbc.update("INSERT INTO academic_year (school_id, year, status, active, created_at) VALUES (?, '2024-2025', 'ACTIVE', true, NOW())", schoolId);
        jdbc.update("INSERT INTO academic_year (school_id, year, status, active, created_at) VALUES (?, '2023-2024', 'COMPLETED', false, NOW())", schoolId);
        log.info("  Seeded: 2 academic years");
    }

    private void seedClasses() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        String[] classes = {"Nursery","LKG","UKG","Class 1","Class 2","Class 3","Class 4","Class 5","Class 6","Class 7","Class 8","Class 9","Class 10","Class 11","Class 12"};
        for (String c : classes) {
            jdbc.update("INSERT INTO school_class (school_id, name, active) VALUES (?, ?, true)", schoolId, c);
        }
        log.info("  Seeded: {} classes", classes.length);
    }

    private void seedSubjects() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        Object[][] subjects = {
            {"Mathematics","MATH"},{"Science","SCI"},{"English","ENG"},{"Hindi","HIN"},
            {"Social Studies","SST"},{"Computer Science","CS"},{"Physical Education","PE"}
        };
        for (Object[] s : subjects) {
            jdbc.update("INSERT INTO subject (school_id, name, code, active) VALUES (?, ?, ?, true)", schoolId, s[0], s[1]);
        }
        log.info("  Seeded: {} subjects", subjects.length);
    }

    private void seedTeachers() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        Object[][] teachers = {
            {"teacher-user-1", "Priya Sharma", "priya.sharma@sps.edu", "9876543201", "T-001", "M.Sc Mathematics"},
            {"teacher-user-2", "Rajesh Kumar", "rajesh.kumar@sps.edu", "9876543202", "T-002", "M.Sc Physics"},
            {"teacher-user-3", "Anita Verma", "anita.verma@sps.edu", "9876543203", "T-003", "M.A English"},
            {"teacher-user-4", "Suresh Patel", "suresh.patel@sps.edu", "9876543204", "T-004", "M.A Hindi"},
            {"teacher-user-5", "Meena Gupta", "meena.gupta@sps.edu", "9876543205", "T-005", "M.A History"},
        };
        for (Object[] t : teachers) {
            jdbc.update("INSERT INTO teacher (user_id, school_id, full_name, email, phone, teacher_code, qualification, active) VALUES (?,?,?,?,?,?,?,true)",
                t[0], schoolId, t[1], t[2], t[3], t[4], t[5]);
        }
        log.info("  Seeded: {} teachers", teachers.length);
    }

    private void seedStudents() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        Object[][] students = {
            {"student-user-1","SPS-2024-001","Aarav Patel","2009-03-15","2024-04-01","Vikram Patel","9876500001","12 MG Road, Delhi"},
            {"student-user-2","SPS-2024-002","Diya Sharma","2009-07-22","2024-04-01","Amit Sharma","9876500002","34 Lajpat Nagar, Delhi"},
            {"student-user-3","SPS-2024-003","Arjun Singh","2009-01-10","2024-04-01","Harpreet Singh","9876500003","56 Karol Bagh, Delhi"},
            {"student-user-4","SPS-2024-004","Ananya Gupta","2009-11-05","2024-04-01","Rakesh Gupta","9876500004","78 Dwarka, Delhi"},
            {"student-user-5","SPS-2024-005","Vihaan Kumar","2009-05-18","2024-04-01","Sanjay Kumar","9876500005","90 Rohini, Delhi"},
            {"student-user-6","SPS-2024-006","Ishita Reddy","2009-09-30","2024-04-01","Venkat Reddy","9876500006","23 Vasant Kunj, Delhi"},
            {"student-user-7","SPS-2024-007","Kabir Joshi","2009-02-14","2024-04-01","Manoj Joshi","9876500007","45 Saket, Delhi"},
            {"student-user-8","SPS-2024-008","Myra Agarwal","2009-06-25","2024-04-01","Pradeep Agarwal","9876500008","67 Janakpuri, Delhi"},
            {"student-user-9","SPS-2024-009","Reyansh Mishra","2009-12-08","2024-04-01","Ashok Mishra","9876500009","89 Pitampura, Delhi"},
            {"student-user-10","SPS-2024-010","Saanvi Nair","2009-04-20","2024-04-01","Suresh Nair","9876500010","11 Greater Kailash, Delhi"},
        };
        for (Object[] s : students) {
            jdbc.update("INSERT INTO student (user_id, school_id, admission_number, full_name, date_of_birth, admission_date, parent_name, parent_mobile, address, status) " +
                "VALUES (?,?,?,?,?::date,?::date,?,?,?,'ACTIVE')",
                s[0], schoolId, s[1], s[2], s[3], s[4], s[5], s[6], s[7]);
        }
        log.info("  Seeded: {} students", students.length);
    }

    private void seedGradingScale() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        jdbc.update("INSERT INTO grading_scale (school_id, name, is_default, active, created_at) VALUES (?,'CBSE Grading Scale',true,true,NOW())", schoolId);
        Long scaleId = jdbc.queryForObject("SELECT id FROM grading_scale WHERE school_id=? AND name='CBSE Grading Scale'", Long.class, schoolId);
        Object[][] grades = {
            {"A1",91,100,10.0,"Outstanding"},{"A2",81,90,9.0,"Excellent"},{"B1",71,80,8.0,"Very Good"},
            {"B2",61,70,7.0,"Good"},{"C1",51,60,6.0,"Above Average"},{"C2",41,50,5.0,"Average"},
            {"D",33,40,4.0,"Below Average"},{"E",0,32,0.0,"Needs Improvement"}
        };
        for (Object[] g : grades) {
            jdbc.update("INSERT INTO grading_scale_entry (grading_scale_id, grade_name, min_percentage, max_percentage, grade_point, description) VALUES (?,?,?,?,?,?)",
                scaleId, g[0], g[1], g[2], g[3], g[4]);
        }
        log.info("  Seeded: CBSE grading scale with 8 grades");
    }

    private void seedFeePlans() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        jdbc.update("INSERT INTO fee_plan (school_id, name, description, active, created_at) VALUES (?,'Day Scholar Plan','For day scholars',true,NOW())", schoolId);
        jdbc.update("INSERT INTO fee_plan (school_id, name, description, active, created_at) VALUES (?,'Transport Plan','For transport students',true,NOW())", schoolId);
        jdbc.update("INSERT INTO fee_plan (school_id, name, description, active, created_at) VALUES (?,'Hosteller Plan','For hostel students',true,NOW())", schoolId);

        Long dayId = jdbc.queryForObject("SELECT id FROM fee_plan WHERE school_id=? AND name='Day Scholar Plan'", Long.class, schoolId);
        Long transId = jdbc.queryForObject("SELECT id FROM fee_plan WHERE school_id=? AND name='Transport Plan'", Long.class, schoolId);
        Long hostelId = jdbc.queryForObject("SELECT id FROM fee_plan WHERE school_id=? AND name='Hosteller Plan'", Long.class, schoolId);

        jdbc.update("INSERT INTO fee_plan_item (fee_plan_id,fee_type,amount,frequency) VALUES (?,'TUITION',5000,'MONTHLY'),(?,'EXAM',2000,'HALF_YEARLY'),(?,'LAB',500,'MONTHLY')", dayId, dayId, dayId);
        jdbc.update("INSERT INTO fee_plan_item (fee_plan_id,fee_type,amount,frequency) VALUES (?,'TUITION',5000,'MONTHLY'),(?,'TRANSPORT',3000,'MONTHLY'),(?,'EXAM',2000,'HALF_YEARLY')", transId, transId, transId);
        jdbc.update("INSERT INTO fee_plan_item (fee_plan_id,fee_type,amount,frequency) VALUES (?,'TUITION',5000,'MONTHLY'),(?,'HOSTEL',8000,'MONTHLY'),(?,'TRANSPORT',1500,'MONTHLY')", hostelId, hostelId, hostelId);
        log.info("  Seeded: 3 fee plans with items");
    }

    private void seedStaff() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        jdbc.update("INSERT INTO staff (school_id,full_name,email,phone,staff_type,active,created_at) VALUES (?,'Ramesh Yadav','ramesh@sps.edu','9876500101','CLERK',true,NOW())", schoolId);
        jdbc.update("INSERT INTO staff (school_id,full_name,email,phone,staff_type,active,created_at) VALUES (?,'Sunita Devi','sunita@sps.edu','9876500102','LIBRARIAN',true,NOW())", schoolId);
        jdbc.update("INSERT INTO staff (school_id,full_name,phone,staff_type,active,created_at) VALUES (?,'Mohan Lal','9876500103','PEON',true,NOW())", schoolId);
        log.info("  Seeded: 3 staff members");
    }

    private void seedPeriods() {
        Long schoolId = jdbc.queryForObject("SELECT id FROM school WHERE school_code='SPS001'", Long.class);
        Object[][] periods = {
            {1,"08:00","08:45",false,"Period 1"},{2,"08:45","09:30",false,"Period 2"},
            {3,"09:30","10:15",false,"Period 3"},{4,"10:15","10:30",true,"Short Break"},
            {5,"10:30","11:15",false,"Period 4"},{6,"11:15","12:00",false,"Period 5"},
            {7,"12:00","12:45",true,"Lunch Break"},{8,"12:45","13:30",false,"Period 6"},
        };
        for (Object[] p : periods) {
            jdbc.update("INSERT INTO period_definition (school_id,period_number,start_time,end_time,is_break,label) VALUES (?,?,?::time,?::time,?,?)",
                schoolId, p[0], p[1], p[2], p[3], p[4]);
        }
        log.info("  Seeded: {} period definitions", periods.length);
    }
}
