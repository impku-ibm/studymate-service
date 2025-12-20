package com.portal.studymate.user.repository;

import com.portal.studymate.user.model.UserRoleId;
import com.portal.studymate.user.model.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, UserRoleId> {
}
