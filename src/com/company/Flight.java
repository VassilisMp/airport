package com.company;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Flight implements Comparable<Flight>, Serializable {
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
        return "Flight{" +
                "date=" + getDate() +
                ", day=" + day +
                ", time=" + getTime() +
                ", seats=" + seats +
                '}';
    }

    @Override
    public int compareTo(Flight o) {
        return dateTime.compareTo(o.dateTime);
    }
}
