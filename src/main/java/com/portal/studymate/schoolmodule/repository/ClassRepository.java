package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.ClassEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

   /* Used in getClasses() */
   List<ClassEntity> findBySchoolId(String schoolId);

   /* Used in addClass() */
   boolean existsBySchoolIdAndClassName(String schoolId, String className);

   /* ================= USED IN addSection() ================= */
   Optional<ClassEntity> findById(Long id);
}

