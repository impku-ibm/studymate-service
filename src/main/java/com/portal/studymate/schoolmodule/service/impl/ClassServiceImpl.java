package com.portal.studymate.schoolmodule.service.impl;

import com.portal.studymate.common.exception.ClassAlreadyExistsException;
import com.portal.studymate.common.exception.ClassNotFoundException;
import com.portal.studymate.common.exception.SectionAlreadyExistsException;
import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.schoolmodule.dtos.ClassResponse;
import com.portal.studymate.schoolmodule.dtos.CreateClassRequest;
import com.portal.studymate.schoolmodule.dtos.CreateSectionRequest;
import com.portal.studymate.schoolmodule.model.ClassEntity;
import com.portal.studymate.schoolmodule.model.SectionEntity;
import com.portal.studymate.schoolmodule.repository.ClassRepository;
import com.portal.studymate.schoolmodule.repository.SectionRepository;
import com.portal.studymate.schoolmodule.service.AcademicYearService;
import com.portal.studymate.schoolmodule.service.ClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassServiceImpl implements ClassService {

   private final ClassRepository classRepository;
   private final JwtContextService jwt;


      public List<ClassResponse> getClasses() {
         return classRepository.findBySchoolId(jwt.getSchoolId())
                               .stream()
                               .map(c -> new ClassResponse(c.getId(),c.getClassName()))
                               .toList();
      }


   public ClassEntity get(Long classId) {
      return classRepository.findById(classId)
                            .orElseThrow(() -> new ClassNotFoundException(classId));
   }

   public void addClass(CreateClassRequest request) {

      String schoolId = jwt.getSchoolId();

      if (classRepository.existsBySchoolIdAndClassName(
         schoolId, request.className())) {
         throw new ClassAlreadyExistsException(request.className());
      }

      ClassEntity cls = new ClassEntity();
      cls.setSchoolId(schoolId);
      cls.setClassName(request.className());

      classRepository.save(cls);
   }
}
