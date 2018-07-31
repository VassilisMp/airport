package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class FlightList extends ArrayList<Flight> implements Cloneable{
    private int totalSeats = 0;
    private boolean dateTimeSorted = false;
    private boolean timeSorted = false;

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

    //returns a deep copy
    @Override
    public FlightList clone() {
        FlightList list = new FlightList();
        for (Flight flight : this) {
            list.add(flight.clone());
        }
        return list;
    }
}
