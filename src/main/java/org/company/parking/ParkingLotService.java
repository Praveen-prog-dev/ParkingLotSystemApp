package org.company.parking;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ParkingLotService {

    private final List<ParkingSpot> spots;

    private final Map<String, ParkingTicket> tickets = new HashMap<>();

    private final Map<String, String> parkedVehicles = new HashMap<>();

    private final ParkingAllocationStrategy allocation;
    private final FeeStrategy feeStrategy;
    private final Clock clock;


    public ParkingLotService(List<ParkingSpot> spots, ParkingAllocationStrategy allocation,
                             FeeStrategy feeStrategy, Clock clock) {
        Objects.requireNonNull(spots);
        this.allocation = Objects.requireNonNull(allocation);
        this.feeStrategy = Objects.requireNonNull(feeStrategy);
        this.clock = Objects.requireNonNull(clock);
        if (spots.stream().anyMatch(Objects::isNull) || spots.stream().map(ParkingSpot::getId)
                .distinct().count() != spots.size()) {
            throw new IllegalArgumentException("Invalid or Duplicate Spot");
        }
        this.spots = List.copyOf(spots);
    }

    //Enter Parking Lot
    public synchronized ParkingTicket parkVehicle(Vehicle vehicle) {
        Objects.requireNonNull(vehicle);

        if (parkedVehicles.containsKey(vehicle.number())) {
            throw new IllegalStateException("Vehicle already Parked");
        }

        ParkingSpot spot = allocation.findSpot(spots, vehicle)
                .orElseThrow(() -> new IllegalStateException("No Suitable spot available"));

        spot.occupy();

        ParkingTicket ticket = new ParkingTicket(
                UUID.randomUUID().toString(),
                vehicle,
                spot.getId(),
                clock.instant()
        );

        tickets.put(ticket.parkingId(), ticket);

        parkedVehicles.put(vehicle.number(), ticket.parkingId());

        return ticket;
    }

    //Exit Parking Lot
    public synchronized ExitReceipt exitVehicle(String parkingId) {
        ParkingTicket ticket = tickets.get(parkingId);

        if (ticket == null) {
            throw new IllegalArgumentException("Invalid ticket");
        }

        Instant exitTime = clock.instant();

        long seconds = Duration.between(ticket.entryTime(), exitTime).toSeconds();

        if (seconds < 0) {
            throw new IllegalStateException("Exit time cannot be before entry");
        }
        long hours = seconds / 3600;

        if (seconds % 3600 > 0) {
            hours++;
        }

        hours = Math.max(1, hours);

        long amount = feeStrategy.calculate(ticket.vehicle().type(), hours);

        ParkingSpot spot = spots.stream()
                .filter(s -> s.getId().equals(ticket.spotId()))
                .findFirst()
                .orElseThrow();

        spot.release();

        tickets.remove(ticket.parkingId());
        parkedVehicles.remove(ticket.vehicle().number());

        return new ExitReceipt(parkingId,
                ticket.vehicle().number(),
                exitTime,
                hours,
                amount);

    }

    //CheckAvailability
    public synchronized long getAvailabilitySpots() {
        return spots.stream()
                .filter(s -> !s.isOccupied())
                .count();
    }
}
