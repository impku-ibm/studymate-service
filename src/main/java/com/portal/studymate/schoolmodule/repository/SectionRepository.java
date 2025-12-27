package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<SectionEntity, Long> {

   List<SectionEntity> findByClassId(Long classId);

   boolean existsByClassIdAndName(Long classId, String name);

   Optional<SectionEntity> findByClassIdAndName(Long classId, String sectionName);
}
