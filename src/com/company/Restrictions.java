package com.company;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class Restrictions {
    LocalDate sDate;
    LocalDate eDate;
    LocalDate[] dates;
    LocalTime sTime;
    LocalTime eTime;
    DayOfWeek[] days;

    public Restrictions(LocalDate sDate, LocalDate eDate, LocalTime sTime, LocalTime eTime, LocalDate ...dates) {
        this.sDate = sDate;
        this.eDate = eDate;
        this.dates = dates;
        this.sTime = sTime;
        this.eTime = eTime;
        this.days = null;
    }

    public void setDays(DayOfWeek ...days) {
        this.days = days;
    }

    List<Flight> flightList (){
        List<Flight> list = new ArrayList<>();
        if(sTime!=null && eTime!=null)
            for(Flight flight: ExcelReader.flightList) {
                if(IsBetweenTime(flight.getTime(), sTime, eTime))
                    list.add(flight);

            }
        if(sDate!=null && eDate!=null)
            for(Flight flight: ExcelReader.flightList) {
                if(flight.getDate().isEqual(sDate)
                        || flight.getDate().isEqual(eDate)
                        || (flight.getDate().isAfter(sDate) && flight.getDate().isBefore(eDate)) )
                    list.add(flight);
                //only if list is sorted by date
                /*if (flight.getDate().isAfter(eDate))
                    break;*/

            }
        if(dates!=null)
            for (LocalDate date: dates)
                for(Flight flight: ExcelReader.flightList) {
                    if(flight.getDate().isEqual(sDate)
                            || flight.getDate().isEqual(eDate)
                            || (flight.getDate().isAfter(sDate) && flight.getDate().isBefore(eDate)) )
                        list.add(flight);

                }
        return list;
    }

    static boolean IsBetweenTime(LocalTime time, LocalTime sTime, LocalTime eTime) {
        return true;
    }
}
