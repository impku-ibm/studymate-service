package com.portal.studymate.school.service;

import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.dtos.CreateSchoolRequest;
import com.portal.studymate.school.dtos.SchoolResponse;
import com.portal.studymate.school.dtos.UpdateSchoolRequest;
import com.portal.studymate.school.model.School;
import com.portal.studymate.school.repository.SchoolRepository;
import com.portal.studymate.school.service.impl.SchoolServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    @Mock private SchoolRepository schoolRepository;
    @InjectMocks private SchoolServiceImpl schoolService;

    private School school;

    @BeforeEach
    void setUp() {
        school = School.builder().id(1L).name("Test School").board("CBSE")
            .city("Delhi").state("Delhi").schoolCode("TST001").active(true).build();
    }

    @Test
    void getCurrentSchool_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            SchoolResponse result = schoolService.getCurrentSchool();
            assertNotNull(result);
        }
    }

    @Test
    void getCurrentSchool_notFound_throws() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(null);
            assertThrows(ResourceNotFoundException.class, () -> schoolService.getCurrentSchool());
        }
    }

    @Test
    void createSchool_success() {
        when(schoolRepository.findByActiveTrue()).thenReturn(Optional.empty());
        when(schoolRepository.existsBySchoolCode("TST001")).thenReturn(false);
        when(schoolRepository.save(any(School.class))).thenAnswer(inv -> {
            School s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        CreateSchoolRequest request = new CreateSchoolRequest();
        request.setName("Test School");
        request.setBoard("CBSE");
        request.setSchoolCode("TST001");

        SchoolResponse result = schoolService.createSchool(request);
        assertNotNull(result);
        verify(schoolRepository).save(any(School.class));
    }

    @Test
    void createSchool_alreadyExists_throws() {
        when(schoolRepository.findByActiveTrue()).thenReturn(Optional.of(school));

        CreateSchoolRequest request = new CreateSchoolRequest();
        assertThrows(ConflictException.class, () -> schoolService.createSchool(request));
    }
}
