package com.portal.studymate.classmanagement.controller;

import com.portal.studymate.classmanagement.dto.ClassResponse;
import com.portal.studymate.classmanagement.dto.CreateClassRequest;
import com.portal.studymate.classmanagement.dto.UpdateClassRequest;
import com.portal.studymate.classmanagement.service.ClassService;
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
class ClassControllerTest {

    @Mock private ClassService classService;
    @InjectMocks private ClassController controller;

    @Test
    void createClass_callsService() {
        ClassResponse response = ClassResponse.builder().id(1L).name("Class 10").active(true).build();
        when(classService.createClass(any(CreateClassRequest.class))).thenReturn(response);

        ClassResponse result = controller.createClass(new CreateClassRequest());
        assertNotNull(result);
        assertEquals("Class 10", result.getName());
    }

    @Test
    void getAllClasses_callsService() {
        when(classService.getAllClasses()).thenReturn(List.of());

        List<ClassResponse> result = controller.getAllClasses();
        assertNotNull(result);
    }

    @Test
    void updateClass_callsService() {
        ClassResponse response = ClassResponse.builder().id(1L).name("Updated").active(true).build();
        when(classService.updateClass(eq(1L), any(UpdateClassRequest.class))).thenReturn(response);

        ClassResponse result = controller.updateClass(1L, new UpdateClassRequest());
        assertNotNull(result);
    }
}
