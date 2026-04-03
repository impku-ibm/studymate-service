package com.portal.studymate.school.controller;

import com.portal.studymate.school.dtos.CreateSchoolRequest;
import com.portal.studymate.school.dtos.SchoolResponse;
import com.portal.studymate.school.dtos.UpdateSchoolRequest;
import com.portal.studymate.school.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolControllerTest {

    @Mock private SchoolService schoolService;
    @InjectMocks private SchoolController controller;

    @Test
    void getSchool_callsService() {
        SchoolResponse response = SchoolResponse.builder().id(1L).name("Test").build();
        when(schoolService.getCurrentSchool()).thenReturn(response);

        SchoolResponse result = controller.getSchool();
        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    void createSchool_callsService() {
        SchoolResponse response = SchoolResponse.builder().id(1L).name("New School").build();
        when(schoolService.createSchool(any(CreateSchoolRequest.class))).thenReturn(response);

        SchoolResponse result = controller.createSchool(new CreateSchoolRequest());
        assertNotNull(result);
    }

    @Test
    void updateSchool_callsService() {
        SchoolResponse response = SchoolResponse.builder().id(1L).name("Updated").build();
        when(schoolService.updateSchool(any(UpdateSchoolRequest.class))).thenReturn(response);

        SchoolResponse result = controller.updateSchool(new UpdateSchoolRequest());
        assertNotNull(result);
    }
}
