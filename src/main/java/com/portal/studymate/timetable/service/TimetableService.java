package com.portal.studymate.timetable.service;

import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.BadRequestException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.repository.SubjectRepository;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import com.portal.studymate.timetable.dto.*;
import com.portal.studymate.timetable.model.PeriodDefinition;
import com.portal.studymate.timetable.model.TimetableEntry;
import com.portal.studymate.timetable.repository.PeriodDefinitionRepository;
import com.portal.studymate.timetable.repository.TimetableEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TimetableService {

    private final PeriodDefinitionRepository periodRepo;
    private final TimetableEntryRepository entryRepo;
    private final SchoolClassRepository classRepo;
    private final SubjectRepository subjectRepo;
    private final TeacherRepository teacherRepo;

    public PeriodDefinitionResponse createPeriod(CreatePeriodDefinitionRequest req) {
        log.info("createPeriod called - periodNumber: {}", req.periodNumber());
        School school = SchoolContext.getSchool();
        PeriodDefinition pd = PeriodDefinition.builder()
            .school(school).periodNumber(req.periodNumber())
            .startTime(req.startTime()).endTime(req.endTime())
            .isBreak(req.isBreak()).label(req.label()).build();
        pd = periodRepo.save(pd);
        return toResponse(pd);
    }

    @Transactional(readOnly = true)
    public List<PeriodDefinitionResponse> getPeriods() {
        log.info("getPeriods called");
        School school = SchoolContext.getSchool();
        return periodRepo.findBySchoolOrderByPeriodNumber(school).stream()
            .map(this::toResponse).toList();
    }

    public void deletePeriod(Long id) {
        log.info("deletePeriod called - id: {}", id);
        periodRepo.deleteById(id);
    }

    public TimetableEntryResponse createEntry(CreateTimetableEntryRequest req) {
        log.info("createEntry called - classId: {}, dayOfWeek: {}", req.classId(), req.dayOfWeek());
        School school = SchoolContext.getSchool();

        // Conflict detection
        List<String> conflicts = validateNoConflicts(req);
        if (!conflicts.isEmpty()) {
            throw new BadRequestException("BAD_REQUEST", "Timetable conflicts: " + String.join("; ", conflicts));
        }

        var period = periodRepo.findById(req.periodDefinitionId())
            .orElseThrow(() -> new ResourceNotFoundException("Period not found"));
        var schoolClass = classRepo.findById(req.classId())
            .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        TimetableEntry entry = TimetableEntry.builder()
            .school(school).periodDefinition(period)
            .schoolClass(schoolClass).section(req.section())
            .dayOfWeek(req.dayOfWeek()).build();

        if (req.subjectId() != null) {
            entry.setSubject(subjectRepo.findById(req.subjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found")));
        }
        if (req.teacherId() != null) {
            entry.setTeacher(teacherRepo.findById(req.teacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found")));
        }

        entry = entryRepo.save(entry);
        return toResponse(entry);
    }

    @Transactional(readOnly = true)
    public List<TimetableEntryResponse> getClassTimetable(Long classId, String section) {
        log.info("getClassTimetable called - classId: {}, section: {}", classId, section);
        return entryRepo.findBySchoolClassIdAndSectionOrderByDayOfWeekAscPeriodDefinitionPeriodNumberAsc(classId, section)
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TimetableEntryResponse> getTeacherTimetable(Long teacherId) {
        log.info("getTeacherTimetable called - teacherId: {}", teacherId);
        return entryRepo.findByTeacherIdOrderByDayOfWeekAscPeriodDefinitionPeriodNumberAsc(teacherId)
            .stream().map(this::toResponse).toList();
    }

    public void deleteEntry(Long id) {
        log.info("deleteEntry called - id: {}", id);
        entryRepo.deleteById(id);
    }

    private List<String> validateNoConflicts(CreateTimetableEntryRequest req) {
        List<String> conflicts = new ArrayList<>();

        // Check teacher double-booking
        if (req.teacherId() != null) {
            var teacherConflicts = entryRepo.findByTeacherIdAndDayOfWeekAndPeriodDefinitionId(
                req.teacherId(), req.dayOfWeek(), req.periodDefinitionId());
            if (!teacherConflicts.isEmpty()) {
                conflicts.add("Teacher is already assigned to another class for this period and day");
            }
        }

        // Check class/section double-booking
        var classConflicts = entryRepo.findBySchoolClassIdAndSectionAndDayOfWeekAndPeriodDefinitionId(
            req.classId(), req.section(), req.dayOfWeek(), req.periodDefinitionId());
        if (!classConflicts.isEmpty()) {
            conflicts.add("This class/section already has an entry for this period and day");
        }

        return conflicts;
    }

    private PeriodDefinitionResponse toResponse(PeriodDefinition pd) {
        return new PeriodDefinitionResponse(pd.getId(), pd.getPeriodNumber(),
            pd.getStartTime(), pd.getEndTime(), pd.isBreak(), pd.getLabel());
    }

    private TimetableEntryResponse toResponse(TimetableEntry e) {
        return new TimetableEntryResponse(e.getId(),
            e.getPeriodDefinition().getPeriodNumber(),
            e.getPeriodDefinition().getStartTime(),
            e.getPeriodDefinition().getEndTime(),
            e.getSchoolClass().getName(), e.getSection(),
            e.getSubject() != null ? e.getSubject().getName() : null,
            e.getTeacher() != null ? e.getTeacher().getFullName() : null,
            e.getDayOfWeek());
    }
}
