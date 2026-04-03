package com.portal.studymate.staff.service;

import com.portal.studymate.attendance.enums.AttendanceStatus;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.staff.dto.CreateStaffRequest;
import com.portal.studymate.staff.enums.StaffType;
import com.portal.studymate.staff.model.Staff;
import com.portal.studymate.staff.model.StaffAttendance;
import com.portal.studymate.staff.repository.StaffAttendanceRepository;
import com.portal.studymate.staff.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock private StaffRepository staffRepository;
    @Mock private StaffAttendanceRepository staffAttendanceRepository;

    @InjectMocks private StaffService staffService;

    private School school;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
    }

    @Test
    void createStaff_success() {
        CreateStaffRequest request = new CreateStaffRequest();
        request.setFullName("Ramesh Kumar");
        request.setStaffType(StaffType.CLERK);

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(staffRepository.save(any(Staff.class))).thenAnswer(inv -> {
                Staff s = inv.getArgument(0);
                s.setId(1L);
                return s;
            });

            var result = staffService.createStaff(request);
            assertEquals("Ramesh Kumar", result.getFullName());
            assertEquals(StaffType.CLERK, result.getStaffType());
        }
    }

    @Test
    void markSelfAttendance_success() {
        Staff staff = Staff.builder().id(1L).school(school).fullName("Test").build();
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(staffAttendanceRepository.findByStaffIdAndAttendanceDate(1L, LocalDate.now()))
            .thenReturn(Optional.empty());
        when(staffAttendanceRepository.save(any())).thenReturn(new StaffAttendance());

        staffService.markSelfAttendance(1L);
        verify(staffAttendanceRepository).save(any(StaffAttendance.class));
    }

    @Test
    void markSelfAttendance_alreadyMarked_skips() {
        Staff staff = Staff.builder().id(1L).school(school).fullName("Test").build();
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(staffAttendanceRepository.findByStaffIdAndAttendanceDate(1L, LocalDate.now()))
            .thenReturn(Optional.of(new StaffAttendance()));

        staffService.markSelfAttendance(1L);
        verify(staffAttendanceRepository, never()).save(any());
    }

    @Test
    void markSelfAttendance_staffNotFound_throws() {
        when(staffRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.markSelfAttendance(99L));
    }
}
