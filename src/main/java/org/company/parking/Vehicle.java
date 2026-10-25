package org.company.parking;

import java.util.Locale;

public record Vehicle(
        String number,
        VehicleType type) {
    public Vehicle {
        if (number == null || number.isBlank() || type == null) {
            throw new IllegalArgumentException("Invalid vehcile details");
        }

        number = number.trim().toUpperCase(Locale.ROOT);
    }
}
