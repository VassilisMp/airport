package com.company;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String args[]) {
        ExcelReader.run();
        //ExcelReader.flightList.removeIf(f -> f.getTime().isBefore(LocalTime.of(8, 0)));
        //System.out.println("Opening chart");
        //new Thread(() -> {Application.launch(FlightChart.class);}).start();
        //GA.ld = LocalDate.of(2018, 7, 30);
        List<Solution> solutionList = new ArrayList<>(31);
        for (int i = 1; i <= 31; i++) {
            System.out.print(i + " of " + 31 + " ");
            GA.flightList = GA.flightList(LocalDate.of(2018, 7, i), null);
            solutionList.add(new Solution(GA.run(GA::getFitnessByDays)));
            System.out.println(solutionList.get(i-1));
        }
        System.out.println("Making List for GA use");
        GA.flightList = ExcelReader.flightList;
        //GA.timesMap = ExcelReader.TimesMap(GA.flightList);
        System.out.println("Running GA");
        Solution best = new Solution(GA.run(GA::getFitnessByDays));
        System.out.println(best);
        int[] deviations = solutionList.stream().mapToInt(sol -> GA.minDif(sol.getTime(), best.getTime())).toArray();
        for (int dev: deviations) {
            System.out.println(dev);
        }
        int sum = Arrays.stream(deviations).sum();
        System.out.println("sum: " + sum/toMin(best.getTime()));
    }
    static int toMin(LocalTime time) {
        return (time.getHour() * 60) + time.getMinute();
    }

    // ################## wrong calculation of standard deviation or whatever I'm trying to find...
}
