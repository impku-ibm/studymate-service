package com.portal.studymate.exam.model;

import com.portal.studymate.student.model.Student;
import com.portal.studymate.teachermgmt.model.Teacher;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_marks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"exam_schedule_id", "student_id"})
})
public class ExamMarks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_schedule_id", nullable = false)
    private ExamSchedule examSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "marks_obtained")
    private Integer marksObtained;

    @Column(nullable = false)
    private Boolean absent = false;

    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entered_by", nullable = false)
    private Teacher enteredBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public ExamMarks() {}

    public ExamMarks(ExamSchedule examSchedule, Student student, Teacher enteredBy) {
        this.examSchedule = examSchedule;
        this.student = student;
        this.enteredBy = enteredBy;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ExamSchedule getExamSchedule() { return examSchedule; }
    public void setExamSchedule(ExamSchedule examSchedule) { this.examSchedule = examSchedule; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Integer getMarksObtained() { return marksObtained; }
    public void setMarksObtained(Integer marksObtained) { this.marksObtained = marksObtained; }

    public Boolean getAbsent() { return absent; }
    public void setAbsent(Boolean absent) { this.absent = absent; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Teacher getEnteredBy() { return enteredBy; }
    public void setEnteredBy(Teacher enteredBy) { this.enteredBy = enteredBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}