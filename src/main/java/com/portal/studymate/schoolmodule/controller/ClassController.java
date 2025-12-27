package com.portal.studymate.schoolmodule.controller;

import com.portal.studymate.schoolmodule.dtos.ClassResponse;
import com.portal.studymate.schoolmodule.dtos.CreateClassRequest;
import com.portal.studymate.schoolmodule.dtos.CreateSectionRequest;
import com.portal.studymate.schoolmodule.dtos.SectionResponse;
import com.portal.studymate.schoolmodule.service.ClassService;
import com.portal.studymate.schoolmodule.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school/classes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class ClassController {
   private final ClassService classService;
   private final SectionService sectionService;

   @GetMapping
   public List<ClassResponse> getClasses() {
      return classService.getClasses();
   }

   @PostMapping
   public void addClass(@RequestBody CreateClassRequest req) {
      classService.addClass(req);
   }

   @GetMapping("/{classId}/sections")
   public List<SectionResponse> getSections(@PathVariable Long classId) {
      return sectionService.getSections(classId);
   }

   @PostMapping("/{classId}/sections")
   public void addSection(
      @PathVariable Long classId,
      @RequestBody CreateSectionRequest req) {

      sectionService.addSection(classId, req);
   }
}
