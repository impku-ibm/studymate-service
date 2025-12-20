package com.portal.studymate.user.repository;

import com.portal.studymate.user.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, UUID> {
   List<ClassRoom> findBySchoolId(UUID schoolid);

   Optional<ClassRoom> findByAcademicYearIdAndClassNameAndSection(
      UUID academicyearid,
      String classname,
      String section
   );
}
