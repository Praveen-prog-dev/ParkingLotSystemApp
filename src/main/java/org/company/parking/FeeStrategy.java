package org.company.parking;

public interface FeeStrategy {

    long calculate(VehicleType type, long hours);
}
