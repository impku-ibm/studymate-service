package com.portal.studymate.staff.service;

import com.portal.studymate.attendance.enums.AttendanceStatus;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.staff.dto.CreateStaffRequest;
import com.portal.studymate.staff.dto.StaffResponse;
import com.portal.studymate.staff.model.Staff;
import com.portal.studymate.staff.model.StaffAttendance;
import com.portal.studymate.staff.repository.StaffAttendanceRepository;
import com.portal.studymate.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StaffService {

    private final StaffRepository staffRepository;
    private final StaffAttendanceRepository staffAttendanceRepository;

    public StaffResponse createStaff(CreateStaffRequest request) {
        log.info("createStaff called - name: {}", request.getFullName());
        School school = SchoolContext.getSchool();
        Staff staff = Staff.builder()
            .school(school)
            .fullName(request.getFullName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .staffType(request.getStaffType())
            .active(true)
            .build();
        staff = staffRepository.save(staff);
        return toResponse(staff);
    }

    @Transactional(readOnly = true)
    public List<StaffResponse> getAllStaff() {
        log.info("getAllStaff called");
        School school = SchoolContext.getSchool();
        return staffRepository.findBySchool(school).stream()
            .map(this::toResponse).toList();
    }

    public StaffResponse updateStaff(Long id, CreateStaffRequest request) {
        log.info("updateStaff called - id: {}", id);
        Staff staff = staffRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        staff.setFullName(request.getFullName());
        staff.setEmail(request.getEmail());
        staff.setPhone(request.getPhone());
        staff.setStaffType(request.getStaffType());
        staff = staffRepository.save(staff);
        return toResponse(staff);
    }

    public void markSelfAttendance(Long staffId) {
        log.info("markSelfAttendance called - staffId: {}", staffId);
        Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        LocalDate today = LocalDate.now();
        if (staffAttendanceRepository.findByStaffIdAndAttendanceDate(staffId, today).isPresent()) {
            return; // already marked
        }

        StaffAttendance attendance = StaffAttendance.builder()
            .staff(staff)
            .attendanceDate(today)
            .status(AttendanceStatus.PRESENT)
            .build();
        staffAttendanceRepository.save(attendance);
    }

    private StaffResponse toResponse(Staff s) {
        return StaffResponse.builder()
            .id(s.getId())
            .fullName(s.getFullName())
            .email(s.getEmail())
            .phone(s.getPhone())
            .staffType(s.getStaffType())
            .active(s.isActive())
            .build();
    }
}
