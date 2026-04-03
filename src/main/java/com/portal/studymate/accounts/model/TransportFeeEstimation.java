package com.portal.studymate.accounts.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transport_fee_estimation")
public class TransportFeeEstimation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "distance_slab", nullable = false, length = 20)
    private String distanceSlab; // "0-5km", "5-10km", "10-20km", "20km+"

    @Column(name = "min_fee", nullable = false)
    private BigDecimal minFee;

    @Column(name = "max_fee", nullable = false)
    private BigDecimal maxFee;

    @Column(name = "bus_route_name", length = 100)
    private String busRouteName;

    @Column(name = "pickup_zone", length = 100)
    private String pickupZone;

    @Column(nullable = false)
    private Boolean active = true;

    // Constructors
    public TransportFeeEstimation() {}

    public TransportFeeEstimation(School school, String distanceSlab, BigDecimal minFee, 
                                 BigDecimal maxFee, String busRouteName, String pickupZone) {
        this.school = school;
        this.distanceSlab = distanceSlab;
        this.minFee = minFee;
        this.maxFee = maxFee;
        this.busRouteName = busRouteName;
        this.pickupZone = pickupZone;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

    public String getDistanceSlab() { return distanceSlab; }
    public void setDistanceSlab(String distanceSlab) { this.distanceSlab = distanceSlab; }

    public BigDecimal getMinFee() { return minFee; }
    public void setMinFee(BigDecimal minFee) { this.minFee = minFee; }

    public BigDecimal getMaxFee() { return maxFee; }
    public void setMaxFee(BigDecimal maxFee) { this.maxFee = maxFee; }

    public String getBusRouteName() { return busRouteName; }
    public void setBusRouteName(String busRouteName) { this.busRouteName = busRouteName; }

    public String getPickupZone() { return pickupZone; }
    public void setPickupZone(String pickupZone) { this.pickupZone = pickupZone; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}