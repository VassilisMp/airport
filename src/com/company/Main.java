package com.company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        ExcelReader.run();
        //System.out.println("Opening chart");
        //new Thread(() -> {Application.launch(FlightChart.class);}).start();
        System.out.println("Running GA");
        //GA.ld = LocalDate.of(2018, 7, 30);
        System.out.println("Making List for GA use");
        GA.flightList = Flight.flightList(LocalDate.of(2018, 7, 30), LocalDate.of(2018, 7, 30));
        int[] best = GA.run(GA::getFitnessByDays);
        SpecWHoursInfo(best[0], best[1], best[2]);
    }

    private static Object[] SpecWHoursInfo(int hours, int mins, int d) {
        int totalSeats = 0;
        int totalFlights = 0;
        //a list containing the flights of these works hours
        List<Flight> sFlightList = new ArrayList<>();
        Object[] array = new Object[3];
        array[0] = totalSeats;
        array[1] = totalFlights;
        array[2] = sFlightList;
        for (Flight flight: ExcelReader.flightList) {
            if(GA.minDif(flight.getTime(), hours, mins)<=(d*60)) {
                totalSeats += flight.getSeats();
                totalFlights++;
                sFlightList.add(flight);
            }
        }
        System.out.println(String.format("%02d:%02d", hours, mins) + "-" + String.format("%02d:%02d", hours+d>23?(hours+d)-24:hours+d, mins));
        System.out.println("totalSeats: " + totalSeats);
        System.out.println("totalFlights: " + totalFlights);
        return array;
    }
}
