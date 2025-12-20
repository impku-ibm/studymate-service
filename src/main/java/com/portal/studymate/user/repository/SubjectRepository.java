package com.portal.studymate.user.repository;

import com.portal.studymate.user.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
   List<Subject> findBySchoolId(UUID schoolid);

}
