package com.company;

import java.util.ArrayList;

public class FlightList extends ArrayList<Flight> {
    private int totalSeats = 0;

    public int getTotalSeats() {
        return totalSeats;
    }

    @Override
    public boolean add(Flight flight) {
        boolean ret = super.add(flight);
        totalSeats += flight.getSeats();
        return ret;
    }
}
