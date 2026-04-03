package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.requests.CreateTransportEstimationRequest;
import com.portal.studymate.accounts.dtos.responses.TransportFeeEstimationResponse;
import com.portal.studymate.accounts.model.TransportFeeEstimation;
import com.portal.studymate.accounts.repository.TransportFeeEstimationRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@Transactional
public class TransportFeeEstimationService {
    
    private final TransportFeeEstimationRepository transportFeeEstimationRepository;

    public TransportFeeEstimationService(TransportFeeEstimationRepository transportFeeEstimationRepository) {
        this.transportFeeEstimationRepository = transportFeeEstimationRepository;
    }

    public TransportFeeEstimationResponse createEstimation(CreateTransportEstimationRequest request) {
        log.info("createEstimation called - distanceSlab: {}", request.distanceSlab());
        var school = SchoolContext.getSchool();
        
        var estimation = new TransportFeeEstimation(
            school,
            request.distanceSlab(),
            request.minFee(),
            request.maxFee(),
            request.busRouteName(),
            request.pickupZone()
        );
        
        estimation = transportFeeEstimationRepository.save(estimation);
        return mapToResponse(estimation);
    }

    @Transactional(readOnly = true)
    public List<TransportFeeEstimationResponse> getAllEstimations() {
        log.info("getAllEstimations called");
        var school = SchoolContext.getSchool();
        
        return transportFeeEstimationRepository.findActiveBySchoolId(school.getId())
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public TransportFeeEstimationResponse getEstimationByDistanceSlab(String distanceSlab) {
        log.info("getEstimationByDistanceSlab called - distanceSlab: {}", distanceSlab);
        var school = SchoolContext.getSchool();
        
        var estimation = transportFeeEstimationRepository
            .findBySchoolIdAndDistanceSlab(school.getId(), distanceSlab)
            .orElseThrow(() -> new ResourceNotFoundException("Transport estimation not found for distance slab: " + distanceSlab));
        
        return mapToResponse(estimation);
    }

    private TransportFeeEstimationResponse mapToResponse(TransportFeeEstimation estimation) {
        // Calculate suggested fee as average of min and max
        var suggestedFee = estimation.getMinFee()
            .add(estimation.getMaxFee())
            .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        
        return new TransportFeeEstimationResponse(
            estimation.getId(),
            estimation.getDistanceSlab(),
            estimation.getMinFee(),
            estimation.getMaxFee(),
            estimation.getBusRouteName(),
            estimation.getPickupZone(),
            suggestedFee
        );
    }
}