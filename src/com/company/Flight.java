package com.company;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Flight {
    private LocalDateTime dateTime;
    private String day;
    private int seats;

    public Flight(LocalDateTime dateTime, String day, int seats) {
        this.dateTime = dateTime;
        this.day = day;
        this.seats = seats;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public String getDay() {
        return day;
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public int getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return getDate() + " " + day + " " + getTime() + " " + seats;
    }
}
