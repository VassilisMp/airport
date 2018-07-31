package com.company;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Flight implements Comparable<Flight>, Cloneable{
    private LocalDateTime dateTime;
    private DayOfWeek day;
    private int seats;

    Flight(LocalDateTime dateTime, DayOfWeek day, int seats) {
        this.dateTime = dateTime;
        this.day = day;
        this.seats = seats;
    }

    LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    DayOfWeek getDay() {
        return day;
    }

    LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    int getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return getDate() + " " + day + " " + getTime() + " " + seats;
    }

    @Override
    public int compareTo(Flight o) {
        return dateTime.compareTo(o.dateTime);
    }

    @Override
    protected Flight clone() {
        Flight clone;
        try{
            clone = (Flight) super.clone();

        }catch(CloneNotSupportedException e){
            throw new RuntimeException(e); // won't happen
        }

        return clone;
    }
}
