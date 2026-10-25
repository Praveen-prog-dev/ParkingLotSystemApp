package org.company.parking;

import java.time.Instant;

public record ExitReceipt(
        String tickedId,
        String vehicleNumber,
        Instant exitTime,
        long chargedHours,
        long totalFee
) {
}
