package com.portal.studymate.classmanagement.service;

import com.portal.studymate.classmanagement.dto.CreateSectionRequest;
import com.portal.studymate.classmanagement.dto.SectionResponse;
import com.portal.studymate.classmanagement.model.ClassSectionTemplate;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.ClassSectionTemplateRepository;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.classmanagement.service.impl.SectionServiceImpl;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock private SchoolClassRepository classRepository;
    @Mock private ClassSectionTemplateRepository sectionRepository;
    @InjectMocks private SectionServiceImpl sectionService;

    @Test
    void createSection_success() {
        SchoolClass sc = SchoolClass.builder().id(1L).name("Class 10").build();
        when(classRepository.findById(1L)).thenReturn(Optional.of(sc));
        when(sectionRepository.findBySchoolClassAndName(sc, "A")).thenReturn(Optional.empty());
        when(sectionRepository.save(any(ClassSectionTemplate.class))).thenAnswer(inv -> {
            ClassSectionTemplate s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        CreateSectionRequest request = new CreateSectionRequest();
        request.setName("A");

        SectionResponse result = sectionService.createSection(1L, request);
        assertNotNull(result);
        verify(sectionRepository).save(any(ClassSectionTemplate.class));
    }

    @Test
    void createSection_classNotFound_throws() {
        when(classRepository.findById(99L)).thenReturn(Optional.empty());
        CreateSectionRequest request = new CreateSectionRequest();
        request.setName("A");
        assertThrows(ResourceNotFoundException.class, () -> sectionService.createSection(99L, request));
    }

    @Test
    void createSection_duplicate_throws() {
        SchoolClass sc = SchoolClass.builder().id(1L).name("Class 10").build();
        when(classRepository.findById(1L)).thenReturn(Optional.of(sc));
        when(sectionRepository.findBySchoolClassAndName(sc, "A"))
            .thenReturn(Optional.of(ClassSectionTemplate.builder().id(1L).build()));

        CreateSectionRequest request = new CreateSectionRequest();
        request.setName("A");
        assertThrows(ConflictException.class, () -> sectionService.createSection(1L, request));
    }

    @Test
    void getSectionsByClass_returnsList() {
        SchoolClass sc = SchoolClass.builder().id(1L).name("Class 10").build();
        when(classRepository.findById(1L)).thenReturn(Optional.of(sc));
        ClassSectionTemplate section = ClassSectionTemplate.builder().id(1L).name("A").active(true).build();
        when(sectionRepository.findBySchoolClass(sc)).thenReturn(List.of(section));

        List<SectionResponse> result = sectionService.getSectionsByClass(1L);
        assertEquals(1, result.size());
    }
}
