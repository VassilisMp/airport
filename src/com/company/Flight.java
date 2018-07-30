package com.company;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    private LocalDateTime dateTime;
    private String day;
    private int seats;

    Flight(LocalDateTime dateTime, String day, int seats) {
        this.dateTime = dateTime;
        this.day = day;
        this.seats = seats;
    }

    LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public String getDay() {
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

    static List<Flight> flightList (LocalDate sDate, LocalDate eDate){
        List<Flight> list = new ArrayList<>();
        if (eDate == null)
            for(Flight flight: ExcelReader.flightList) {
                if(flight.getDate().isEqual(sDate))
                    list.add(flight);
            }
        else
            for(Flight flight: ExcelReader.flightList) {
                if(flight.getDate().isEqual(sDate)
                        || flight.getDate().isEqual(eDate)
                        || (flight.getDate().isAfter(sDate) && flight.getDate().isBefore(eDate)) )
                    list.add(flight);
            }
        return list;
    }
}
