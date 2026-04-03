package com.portal.studymate.student.repository;

import com.portal.studymate.student.model.TransferCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransferCertificateRepository extends JpaRepository<TransferCertificate, Long> {
    Optional<TransferCertificate> findByStudentId(Long studentId);
}
