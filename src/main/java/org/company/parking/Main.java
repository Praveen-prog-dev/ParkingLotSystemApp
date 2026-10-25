package org.company.parking;

import java.time.Clock;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<ParkingSpot> spots = List.of(
                new ParkingSpot("S1", SpotType.SMALL),
                new ParkingSpot("M1", SpotType.MEDIUM),
                new ParkingSpot("L1", SpotType.LARGE)
        );

        ParkingLotService lot = new ParkingLotService(spots,
                new SmallestFitStrategy(),
                new HourlyFeeStrategy(),
                Clock.systemUTC()
                );

        Vehicle car = new Vehicle("AP40123", VehicleType.CAR);

        ParkingTicket ticket = lot.parkVehicle(car);

        System.out.println("Allocated: " +ticket.parkingId());
        System.out.println("Available: " +lot.getAvailabilitySpots());

        ExitReceipt receipt = lot.exitVehicle(ticket.parkingId());

        System.out.println("Fee Rs." +receipt.totalFee());
        System.out.println("Available: " +lot.getAvailabilitySpots());
    }
}