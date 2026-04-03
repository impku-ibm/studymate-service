package com.portal.studymate.attendance.model;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.attendance.enums.AttendanceStatus;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.teachermgmt.model.Teacher;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_attendance", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "attendance_date"})
})
public class StudentAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by", nullable = false)
    private Teacher markedBy;

    @Column(nullable = false, length = 10)
    private String section;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public StudentAttendance() {}

    public StudentAttendance(Student student, AcademicYear academicYear, LocalDate attendanceDate,
                           AttendanceStatus status, Teacher markedBy, String section) {
        this.student = student;
        this.academicYear = academicYear;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.markedBy = markedBy;
        this.section = section;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public AcademicYear getAcademicYear() { return academicYear; }
    public void setAcademicYear(AcademicYear academicYear) { this.academicYear = academicYear; }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }

    public Teacher getMarkedBy() { return markedBy; }
    public void setMarkedBy(Teacher markedBy) { this.markedBy = markedBy; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}