package com.company;

import java.time.LocalDateTime;

public class Flight {
    //private LocalDateTime dateTime;
    private Date date;
    private String day;
    private Time time;
    private int seats;

    public Flight(Date date, String day, Time time, int seats) {
        this.date = date;
        this.day = day;
        this.time = time;
        this.seats = seats;
    }

    /*public Flight(LocalDateTime dateTime, String day, Time time, int seats) {
        this.dateTime = dateTime;
        this.day = day;
        this.time = time;
        this.seats = seats;
    }*/

    public Date getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public Time getTime() {
        return time;
    }

    public int getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return date + " " + day + " " + time + " " + seats;
    }
}
