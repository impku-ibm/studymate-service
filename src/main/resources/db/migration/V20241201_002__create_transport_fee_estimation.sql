-- Create transport_fee_estimation table
CREATE TABLE transport_fee_estimation (
    id BIGSERIAL PRIMARY KEY,
    school_id BIGINT NOT NULL,
    distance_slab VARCHAR(20) NOT NULL,
    min_fee DECIMAL(10,2) NOT NULL,
    max_fee DECIMAL(10,2) NOT NULL,
    bus_route_name VARCHAR(100),
    pickup_zone VARCHAR(100),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_transport_estimation_school FOREIGN KEY (school_id) REFERENCES school(id),
    CONSTRAINT chk_min_max_fee CHECK (min_fee <= max_fee),
    CONSTRAINT uk_transport_estimation_school_slab UNIQUE (school_id, distance_slab)
);

-- Create index for performance
CREATE INDEX idx_transport_estimation_school_active ON transport_fee_estimation(school_id, active);