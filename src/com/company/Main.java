package com.company;

import java.time.LocalDate;

public class Main {
    public static void main(String args[]) {
        ExcelReader.run();
        //System.out.println("Opening chart");
        //new Thread(() -> {Application.launch(FlightChart.class);}).start();
        //GA.ld = LocalDate.of(2018, 7, 30);
        System.out.println("Making List for GA use");
        GA.flightList = GA.flightList(LocalDate.of(2018, 7, 1), LocalDate.of(2018, 7, 31));
        //GA.timesMap = ExcelReader.TimesMap(GA.flightList);
        System.out.println("Running GA");
        int[] best = GA.run(GA::getFitnessByDays);
        //###############
        // I must make a class for "best"
        SpecWHoursInfo(best[0], best[1], best[2]);
        System.out.println();
        System.out.println(((FlightList)ExcelReader.flightList).getTotalSeats());
    }

    private static FlightList SpecWHoursInfo(int hours, int mins, int d) {
        //a list containing the flights of these works hours
        FlightList sFlightList = new FlightList();
        for (Flight flight: ExcelReader.flightList) {
            if(GA.minDif(flight.getTime(), hours, mins)<=(d*60)) {
                sFlightList.add(flight);
            }
        }
        System.out.println(String.format("%02d:%02d", hours, mins) + "-" + String.format("%02d:%02d", hours+d>23?(hours+d)-24:hours+d, mins) + "  " + d + " hours");
        System.out.println("totalSeats: " + sFlightList.getTotalSeats());
        System.out.println("totalFlights: " + sFlightList.size());
        return sFlightList;
    }
}
