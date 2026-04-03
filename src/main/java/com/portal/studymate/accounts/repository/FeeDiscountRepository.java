package com.portal.studymate.accounts.repository;

import com.portal.studymate.accounts.model.FeeDiscount;
import com.portal.studymate.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeeDiscountRepository extends JpaRepository<FeeDiscount, Long> {
    List<FeeDiscount> findByStudentAndActiveTrue(Student student);
    List<FeeDiscount> findByStudentIdAndAcademicYearIdAndActiveTrue(Long studentId, Long academicYearId);
}
