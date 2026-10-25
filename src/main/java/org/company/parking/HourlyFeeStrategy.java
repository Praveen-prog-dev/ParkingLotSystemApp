package org.company.parking;

public class HourlyFeeStrategy implements FeeStrategy {

    @Override
    public long calculate(VehicleType type, long hours) {

        if (hours < 1) {
            throw new IllegalArgumentException("Invalid Parking Hours");
        }

        long rate = switch (type) {
            case MOTORCYCLE -> 10;
            case CAR -> 20;
            case TRUCK -> 30;
        };
        return Math.multiplyExact(rate, hours);
    }
}
