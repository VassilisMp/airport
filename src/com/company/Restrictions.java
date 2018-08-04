package com.company;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class Restrictions implements Predicate<Flight>{
    private LocalDate sDate;
    private LocalDate eDate;
    private LocalDate[] dates;
    private LocalTime sTime;
    private LocalTime eTime;
    private DayOfWeek[] days;

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

    @Override
    public boolean test(Flight flight) {
        if (days!=null) {
            for (DayOfWeek day: days)
                if (flight.getDay().equals(day))
                    return true;
        }
        if(dates!=null)
            for (LocalDate date: dates)
                if(flight.getDate().isEqual(date))
                    return true;
        if(sTime!=null && eTime!=null)
            if(IsBetweenTime(flight.getTime(), sTime, eTime))
                return true;
        if(sDate!=null && eDate!=null)
            return flight.getDate().isEqual(sDate)
                || flight.getDate().isEqual(eDate)
                || (flight.getDate().isAfter(sDate) && flight.getDate().isBefore(eDate));
        return false;
    }
}
