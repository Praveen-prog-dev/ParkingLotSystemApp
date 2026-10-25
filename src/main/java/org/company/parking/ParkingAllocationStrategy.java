package org.company.parking;

import java.util.List;
import java.util.Optional;

public interface ParkingAllocationStrategy {

    Optional<ParkingSpot> findSpot(List<ParkingSpot> spots, Vehicle vehicle);
}
