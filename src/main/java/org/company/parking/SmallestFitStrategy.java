package org.company.parking;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SmallestFitStrategy implements ParkingAllocationStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingSpot> spots, Vehicle vehicle) {

        SpotType required = switch (vehicle.type()) {
            case MOTORCYCLE -> SpotType.SMALL;
            case CAR -> SpotType.MEDIUM;
            case TRUCK -> SpotType.LARGE;
        };

        return spots.stream()
                .filter(s -> !s.isOccupied())
                .filter(s -> s.getType().ordinal() >= required.ordinal())
                .min(Comparator.comparingInt(s -> s.getType().ordinal()));
    }
}
