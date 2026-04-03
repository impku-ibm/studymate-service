package com.portal.studymate.timetable.controller;

import com.portal.studymate.timetable.dto.*;
import com.portal.studymate.timetable.service.TimetableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimetableControllerTest {
    @Mock private TimetableService timetableService;
    @InjectMocks private TimetableController controller;

    @Test
    void createPeriod_returnsCreated() {
        var response = new PeriodDefinitionResponse(1L, 1, LocalTime.of(8, 0), LocalTime.of(8, 45), false, "Period 1");
        when(timetableService.createPeriod(any())).thenReturn(response);
        var result = controller.createPeriod(new CreatePeriodDefinitionRequest(1, LocalTime.of(8, 0), LocalTime.of(8, 45), false, "Period 1"));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void getPeriods_returnsOk() {
        when(timetableService.getPeriods()).thenReturn(List.of());
        var result = controller.getPeriods();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getClassTimetable_returnsOk() {
        when(timetableService.getClassTimetable(1L, "A")).thenReturn(List.of());
        var result = controller.getClassTimetable(1L, "A");
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
