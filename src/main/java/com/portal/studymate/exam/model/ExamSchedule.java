package com.portal.studymate.exam.model;

import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.subject.model.Subject;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exam_schedules", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"exam_id", "school_class_id", "section", "subject_id"})
})
public class ExamSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass;

    @Column(nullable = false, length = 10)
    private String section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks;

    @Column(name = "pass_marks", nullable = false)
    private Integer passMarks;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @OneToMany(mappedBy = "examSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExamMarks> examMarks;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public ExamSchedule() {}

    public ExamSchedule(Exam exam, SchoolClass schoolClass, String section, Subject subject,
                       LocalDate examDate, Integer maxMarks, Integer passMarks, Integer durationMinutes) {
        this.exam = exam;
        this.schoolClass = schoolClass;
        this.section = section;
        this.subject = subject;
        this.examDate = examDate;
        this.maxMarks = maxMarks;
        this.passMarks = passMarks;
        this.durationMinutes = durationMinutes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public Integer getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Integer maxMarks) { this.maxMarks = maxMarks; }

    public Integer getPassMarks() { return passMarks; }
    public void setPassMarks(Integer passMarks) { this.passMarks = passMarks; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public List<ExamMarks> getExamMarks() { return examMarks; }
    public void setExamMarks(List<ExamMarks> examMarks) { this.examMarks = examMarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}