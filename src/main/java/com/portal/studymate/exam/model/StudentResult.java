package com.portal.studymate.exam.model;

import com.portal.studymate.exam.enums.ResultStatus;
import com.portal.studymate.student.model.Student;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_results", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "exam_id"})
})
public class StudentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(name = "total_marks", nullable = false)
    private Integer totalMarks = 0;

    @Column(name = "max_possible_marks", nullable = false)
    private Integer maxPossibleMarks = 0;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal percentage = BigDecimal.ZERO;

    @Column(length = 5)
    private String grade;

    @Column(name = "rank_in_class")
    private Integer rankInClass;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status")
    private ResultStatus resultStatus;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public StudentResult() {}

    public StudentResult(Student student, Exam exam) {
        this.student = student;
        this.exam = exam;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer totalMarks) { this.totalMarks = totalMarks; }

    public Integer getMaxPossibleMarks() { return maxPossibleMarks; }
    public void setMaxPossibleMarks(Integer maxPossibleMarks) { this.maxPossibleMarks = maxPossibleMarks; }

    public BigDecimal getPercentage() { return percentage; }
    public void setPercentage(BigDecimal percentage) { this.percentage = percentage; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Integer getRankInClass() { return rankInClass; }
    public void setRankInClass(Integer rankInClass) { this.rankInClass = rankInClass; }

    public ResultStatus getResultStatus() { return resultStatus; }
    public void setResultStatus(ResultStatus resultStatus) { this.resultStatus = resultStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}