package com.portal.studymate.accounts.repository;

import com.portal.studymate.accounts.model.TransportFeeEstimation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransportFeeEstimationRepository extends JpaRepository<TransportFeeEstimation, Long> {
    
    @Query("SELECT t FROM TransportFeeEstimation t WHERE t.school.id = :schoolId AND t.active = true")
    List<TransportFeeEstimation> findActiveBySchoolId(@Param("schoolId") Long schoolId);
    
    @Query("SELECT t FROM TransportFeeEstimation t WHERE t.school.id = :schoolId AND t.id = :id")
    Optional<TransportFeeEstimation> findByIdAndSchoolId(@Param("id") Long id, @Param("schoolId") Long schoolId);
    
    @Query("SELECT t FROM TransportFeeEstimation t WHERE t.school.id = :schoolId AND t.distanceSlab = :distanceSlab AND t.active = true")
    Optional<TransportFeeEstimation> findBySchoolIdAndDistanceSlab(@Param("schoolId") Long schoolId, 
                                                                  @Param("distanceSlab") String distanceSlab);
}