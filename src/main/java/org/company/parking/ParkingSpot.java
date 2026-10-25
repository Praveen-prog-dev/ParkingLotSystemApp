package org.company.parking;

public class ParkingSpot {

    private final String id;
    private SpotType type;
    private boolean occupied;

    public ParkingSpot(String id, SpotType type) {

        if (id == null || id.isBlank() || type == null) {
            throw new IllegalArgumentException("Invalid Spot");
        }
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public SpotType getType() {
        return type;
    }

    public boolean isOccupied() {
        return occupied;
    }

    void occupy() {
        if (occupied) {
            throw new IllegalArgumentException("Spot Occupied");
        }
        occupied = true;
    }

    void release() {
        if (!occupied) {
            throw new IllegalArgumentException("Spot already free");
        }
        occupied = false;
    }

}
