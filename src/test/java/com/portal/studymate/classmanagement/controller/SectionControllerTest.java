package com.portal.studymate.classmanagement.controller;

import com.portal.studymate.classmanagement.dto.CreateSectionRequest;
import com.portal.studymate.classmanagement.dto.SectionResponse;
import com.portal.studymate.classmanagement.dto.UpdateSectionRequest;
import com.portal.studymate.classmanagement.service.SectionService;
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
class SectionControllerTest {

    @Mock private SectionService sectionService;
    @InjectMocks private SectionController controller;

    @Test
    void createSection_callsService() {
        SectionResponse response = SectionResponse.builder().id(1L).name("A").active(true).build();
        when(sectionService.createSection(eq(1L), any(CreateSectionRequest.class))).thenReturn(response);

        SectionResponse result = controller.createSection(1L, new CreateSectionRequest());
        assertNotNull(result);
        assertEquals("A", result.getName());
    }

    @Test
    void getSectionsByClass_callsService() {
        when(sectionService.getSectionsByClass(1L)).thenReturn(List.of());

        List<SectionResponse> result = controller.getSections(1L);
        assertNotNull(result);
    }

    @Test
    void updateSection_callsService() {
        SectionResponse response = SectionResponse.builder().id(1L).name("B").active(true).build();
        when(sectionService.updateSection(eq(1L), any(UpdateSectionRequest.class))).thenReturn(response);

        SectionResponse result = controller.updateSection(1L, new UpdateSectionRequest());
        assertNotNull(result);
    }
}
