package com.portal.studymate.subject.repository;

import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

   List<Subject> findBySchool(School school);

   Optional<Subject> findBySchoolAndCode(School school, String code);
}

