package com.portal.studymate.timetable.service;

import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.BadRequestException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.repository.SubjectRepository;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import com.portal.studymate.timetable.dto.CreateTimetableEntryRequest;
import com.portal.studymate.timetable.model.TimetableEntry;
import com.portal.studymate.timetable.repository.PeriodDefinitionRepository;
import com.portal.studymate.timetable.repository.TimetableEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {

    @Mock private PeriodDefinitionRepository periodRepo;
    @Mock private TimetableEntryRepository entryRepo;
    @Mock private SchoolClassRepository classRepo;
    @Mock private SubjectRepository subjectRepo;
    @Mock private TeacherRepository teacherRepo;

    @InjectMocks private TimetableService timetableService;

    private School school;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
    }

    @Test
    void createEntry_detectsTeacherConflict() {
        var request = new CreateTimetableEntryRequest(1L, 1L, "A", 1L, 1L, 1);

        when(entryRepo.findByTeacherIdAndDayOfWeekAndPeriodDefinitionId(1L, 1, 1L))
            .thenReturn(List.of(new TimetableEntry()));

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            BadRequestException ex = assertThrows(BadRequestException.class,
                () -> timetableService.createEntry(request));
            assertTrue(ex.getMessage().contains("Teacher is already assigned"));
        }
    }

    @Test
    void createEntry_detectsClassConflict() {
        var request = new CreateTimetableEntryRequest(1L, 1L, "A", null, null, 1);

        when(entryRepo.findBySchoolClassIdAndSectionAndDayOfWeekAndPeriodDefinitionId(1L, "A", 1, 1L))
            .thenReturn(List.of(new TimetableEntry()));

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            BadRequestException ex = assertThrows(BadRequestException.class,
                () -> timetableService.createEntry(request));
            assertTrue(ex.getMessage().contains("class/section already has an entry"));
        }
    }
}
