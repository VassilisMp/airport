package com.company;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelReader {
    public static final String XLSX_FILE_PATH = "./Multi-Day Flightplan.xlsx";
    public static Map<LocalTime, Integer> FlightMap;

    public static void run() {

        File file = new File(XLSX_FILE_PATH);
        if (!file.exists()) {
            System.out.println("file doesn't exist\nexiting..");
            System.exit(-1);
        }
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        String date;
        String Day;
        String STA;
        int value;
        int seats;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyyHH:mm");
        List<Flight> flightList = new ArrayList<>();
        System.out.println("Inserting times in list..");
        for (Row row: sheet) {
            //STA retrieve
            Cell cell = row.getCell(9);
            STA = dataFormatter.formatCellValue(cell);
            if (STA.equals("") || STA.equals("STA"))
                continue;
            //seats retrieve
            cell = row.getCell(11);
            seats = (int)cell.getNumericCellValue();
            if (seats<=0)
                continue;
            //day retrieve
            cell = row.getCell(2);
            Day = dataFormatter.formatCellValue(cell);
            //System.out.println(Day);
            //date retrieve
            cell = row.getCell(1);
            date = dataFormatter.formatCellValue(cell);
            //System.out.println(date);
            LocalDateTime dateTime = LocalDateTime.parse(date + STA, formatter);
            flightList.add(new Flight(dateTime, Day, seats));
        }
        System.out.println("sorting list..");
        flightList.sort((f1, f2) -> {
            if(f1.getTime().equals(f2.getTime())){
                return 0;
            }
            return f1.getTime().compareTo(f2.getTime());
        });
        FlightMap = new HashMap<>(flightList.size());
        System.out.println("creating map..");
        for (Flight flight: flightList) {
            //System.out.println(flight);
            if(FlightMap.containsKey(flight.getTime())) {
                value = FlightMap.get(flight.getTime());
                FlightMap.replace(flight.getTime(), value+flight.getSeats());
                if(value == FlightMap.get(flight.getTime()))
                    System.out.println("value didn't change");
            }
            else
                FlightMap.put(flight.getTime(), flight.getSeats());
        }
        int flights = 0;
        for(Map.Entry<LocalTime, Integer> entry: FlightMap.entrySet()) {
            //System.out.println(entry.getKey() + "  " + entry.getValue());
            flights += entry.getValue();
        }
        System.out.println("map size:" + FlightMap.size());
        System.out.println("seats num:" + flights);

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            System.out.print("Couldn't close Workbook");
            e.printStackTrace();
        }

        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        LocalDateTime dateTime1= LocalDateTime.parse("2014-11-25 19:00:00", formatter);
        LocalDateTime dateTime2= LocalDateTime.parse("2014-11-26 00:30:00", formatter);

        long diffInMilli = java.time.Duration.between(dateTime1, dateTime2).toMillis();
        long diffInSeconds = java.time.Duration.between(dateTime1, dateTime2).getSeconds();
        long diffInMinutes = java.time.Duration.between(dateTime1, dateTime2).toMinutes();
        long diffInHours = java.time.Duration.between(dateTime1, dateTime2).toHours();
        System.out.println("Hours: " + diffInHours);
        Time time1 = new Time(20, 40);
        System.out.println(time1.DifferenceMin(22, 46));*/

    }
}
