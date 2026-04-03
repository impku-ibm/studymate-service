package com.portal.studymate.staff.controller;

import com.portal.studymate.staff.dto.CreateStaffRequest;
import com.portal.studymate.staff.dto.StaffResponse;
import com.portal.studymate.staff.enums.StaffType;
import com.portal.studymate.staff.service.StaffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffControllerTest {
    @Mock private StaffService staffService;
    @InjectMocks private StaffController controller;

    @Test
    void create_returnsCreated() {
        var response = StaffResponse.builder().id(1L).fullName("Ramesh").staffType(StaffType.CLERK).active(true).build();
        when(staffService.createStaff(any())).thenReturn(response);
        var result = controller.create(new CreateStaffRequest());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Ramesh", result.getBody().getFullName());
    }

    @Test
    void getAll_returnsOk() {
        when(staffService.getAllStaff()).thenReturn(List.of(
            StaffResponse.builder().id(1L).fullName("A").build()));
        var result = controller.getAll();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }
}
