package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class FlightList extends ArrayList<Flight> implements Serializable {
    private int totalSeats = 0;
    private boolean dateTimeSorted = false;
    private boolean timeSorted = false;

    FlightList() {
        super();
    }

    FlightList(int initialCapacity) {
        super(initialCapacity);
    }

    FlightList(Collection<? extends Flight> c) {
        super(c);
        this.forEach((f)-> totalSeats+=f.getSeats());
    }

    int getTotalSeats() {
        return totalSeats;
    }

    @Override
    public boolean add(Flight flight) {
        boolean ret = super.add(flight);
        totalSeats += flight.getSeats();
        return ret;
    }

    void sortByDateTime() {
        Collections.sort(this);
        dateTimeSorted = true;
    }

    boolean isSortedByDateTime(){
        return dateTimeSorted;
    }

    void sortByTime() {
        Comparator<Flight> comparator = Comparator.comparing(Flight::getTime);
        this.sort(comparator);
        timeSorted = true;
    }

    boolean isSortedByTime(){
        return timeSorted;
    }
}
