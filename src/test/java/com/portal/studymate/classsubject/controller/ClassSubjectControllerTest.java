package com.portal.studymate.classsubject.controller;

import com.portal.studymate.classsubject.dto.AssignSubjectsRequest;
import com.portal.studymate.classsubject.dto.ClassSubjectResponse;
import com.portal.studymate.classsubject.service.ClassSubjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassSubjectControllerTest {
    @Mock private ClassSubjectService classSubjectService;
    @InjectMocks private ClassSubjectController controller;

    @Test
    void assignSubjects_returnsList() {
        when(classSubjectService.assignSubjects(any())).thenReturn(List.of());
        var result = controller.assignSubjects(new AssignSubjectsRequest());
        assertNotNull(result);
    }

    @Test
    void getSubjectsForClass_returnsList() {
        when(classSubjectService.getSubjectsForClass(1L)).thenReturn(List.of());
        assertEquals(0, controller.getSubjectsForClass(1L).size());
    }
}
