package com.portal.studymate.subject.controller;

import com.portal.studymate.subject.dto.CreateSubjectRequest;
import com.portal.studymate.subject.dto.SubjectResponse;
import com.portal.studymate.subject.dto.UpdateSubjectRequest;
import com.portal.studymate.subject.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    @Mock private SubjectService subjectService;
    @InjectMocks private SubjectController controller;

    @Test
    void createSubject_callsService() {
        SubjectResponse response = SubjectResponse.builder().id(1L).name("Math").code("MATH").active(true).build();
        when(subjectService.createSubject(any(CreateSubjectRequest.class))).thenReturn(response);

        SubjectResponse result = controller.createSubject(new CreateSubjectRequest());
        assertNotNull(result);
        assertEquals("Math", result.getName());
    }

    @Test
    void getAllSubjects_callsService() {
        when(subjectService.getAllSubjects()).thenReturn(List.of());

        List<SubjectResponse> result = controller.getAllSubjects();
        assertNotNull(result);
    }

    @Test
    void updateSubject_callsService() {
        SubjectResponse response = SubjectResponse.builder().id(1L).name("Updated").code("UPD").active(true).build();
        when(subjectService.updateSubject(eq(1L), any(UpdateSubjectRequest.class))).thenReturn(response);

        SubjectResponse result = controller.updateSubject(1L, new UpdateSubjectRequest());
        assertNotNull(result);
    }
}
