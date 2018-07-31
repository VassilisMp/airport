package com.company;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        LocalTime t1 = LocalTime.of(23, 0);
        LocalTime t2 = LocalTime.of(0, 0);
        System.out.println(t1.isAfter(t2));
        ExcelReader.run();
        System.out.println(ExcelReader.flightList.size());
        System.out.println(ExcelReader.flightList.get(0).getDay());
        //System.out.println("Opening chart");
        //new Thread(() -> {Application.launch(FlightChart.class);}).start();
        //GA.ld = LocalDate.of(2018, 7, 30);
        List<Solution> solutionList = new ArrayList<>(31);
        for (int i = 1; i <= 31; i++) {
            System.out.println(i + " of " + 31);
            GA.flightList = GA.flightList(LocalDate.of(2018, 7, i), null);
            solutionList.add(GA.run(GA::getFitnessByDays));
        }
        System.out.println("Making List for GA use");
        GA.flightList = GA.flightList(LocalDate.of(2018, 7, 1), LocalDate.of(2018, 7, 31));
        //GA.timesMap = ExcelReader.TimesMap(GA.flightList);
        System.out.println("Running GA");
        Solution best = GA.run(GA::getFitnessByDays);
        //###############
        // I must make a class for "best"
        specWHoursInfo(best);
        System.out.println();
        System.out.println(((FlightList)ExcelReader.flightList).getTotalSeats());
    }

    private static FlightList specWHoursInfo(Solution sol) {
        LocalTime time = sol.getTime();
        int d = sol.getDuration();
        int fitness = sol.getFitness();
        //a list containing the flights of these works hours
        FlightList flightList = new FlightList();
        for (Flight flight: ExcelReader.flightList) {
            if(GA.minDif(flight.getTime(), time)<=(d*60)) {
                flightList.add(flight);
            }
        }
        //System.out.println(String.format("%02d:%02d", hours, mins) + "-" + String.format("%02d:%02d", hours+d>23?(hours+d)-24:hours+d, mins) + "  " + d + " hours");
        System.out.println(time + "-" + time.plusHours(4) + "  " + d + " hours");
        System.out.println("totalSeats: " + flightList.getTotalSeats());
        System.out.println("totalFlights: " + flightList.size());
        return flightList;
    }
}
