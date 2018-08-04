package com.company;

public class Main {

    public static void main(String args[]) {
        ExcelReader.run();
        //ExcelReader.flightList.removeIf(f -> f.getTime().isBefore(LocalTime.of(8, 0)));
        //System.out.println("Opening chart");
        //new Thread(() -> {Application.launch(FlightChart.class);}).start();
        //GA.ld = LocalDate.of(2018, 7, 30);
        GA.flightList = ExcelReader.flightList;
        Solution best = new Solution(GA.run(GA::getFitnessByDays));
        System.out.println("My best: " + best);
    }


}
