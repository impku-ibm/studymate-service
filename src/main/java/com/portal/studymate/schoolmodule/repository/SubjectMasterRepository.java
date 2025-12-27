package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.SubjectMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectMasterRepository extends JpaRepository<SubjectMaster,Long> {
   boolean existsBySchoolIdAndNameIgnoreCase(String schoolId, String name);

   Optional<SubjectMaster> findByIdAndSchoolId(Long id, String schoolId);

   List<SubjectMaster> findAllBySchoolIdAndActiveTrue(String schoolId);

   Optional<SubjectMaster> findByIdAndActiveTrue(Long id);
}
