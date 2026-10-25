package org.company.parking;

import java.time.Instant;

public record ParkingTicket(
        String parkingId,
        Vehicle vehicle,
        String spotId,
        Instant entryTime) {
}
