package com.company;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ExcelReader {
    public static final String SAMPLE_XLSX_FILE_PATH = "./Multi-Day Flightplan.xlsx";
    public static Map<Time, Integer> FlightMap;

    public static Map run() {

        File file = new File(SAMPLE_XLSX_FILE_PATH);
        if (!file.exists()) {
            System.out.println("file doesn't exist\nexiting..");
           // return;
        }
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            //return;
        }

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        // String to be scanned to find the pattern.
        String timePattern = "([\\d]{2}):([\\d]{2})";
        // Create a Pattern object
        Pattern r = Pattern.compile(timePattern);
        // Now create matcher object.
        Matcher m;
        Pattern datePat = Pattern.compile("([\\d]+)\\.([\\d]+)\\.([\\d]+)");
        String date;
        Date Ddate;
        int day = 0, month = 0, year = 0;
        String Day;
        String STD;
        int hours, minutes;
        int value;
        int seats;
        Time time;
        Flight flight;
        List<Flight> flightList = new ArrayList<>();
        System.out.println("Inserting times in list..");
        for (Row row: sheet) {
            //STD retrieve
            Cell cell = row.getCell(9);
            STD = dataFormatter.formatCellValue(cell);
            if (STD.equals("") || STD.equals("STA"))
                continue;
            //seats retrieve
            cell = row.getCell(11);
            seats = (int)cell.getNumericCellValue();
            if (seats<=0)
                continue;
            //STD...
            m = r.matcher(STD);
            if (m.find( )) {
                hours = Integer.parseInt(m.group(1));
                minutes = Integer.parseInt(m.group(2));
                time = new Time(hours, minutes);
                //System.out.println(time);
                //System.out.println(seats);
            }
            else {
                System.out.println("couldn't match time String");
                continue;
            }
            //day retrieve
            cell = row.getCell(2);
            Day = dataFormatter.formatCellValue(cell);
            //System.out.println(Day);
            //date retrieve
            cell = row.getCell(1);
            date = dataFormatter.formatCellValue(cell);
            //System.out.println(date);
            m = datePat.matcher(date);
            if (m.find( )) {
                day = Integer.parseInt(m.group(1));
                month = Integer.parseInt(m.group(2));
                year = Integer.parseInt(m.group(3));
                //System.out.println(day + "-" + month + "-" + year);
                Ddate = new Date(day, month, year);
            }
            else {
                System.out.println("couldn't match date String");
                continue;
            }
            /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM-dd HH:mm:ss");
            LocalDateTime dateTime1= LocalDateTime.parse("2014-11-25 19:00:00", formatter);*/
            flightList.add(new Flight(Ddate, Day, time, seats));
        }
        System.out.println("sorting list..");
        flightList.sort((f1, f2) -> {
            if(f1.getTime().equals(f2.getTime())){
                return 0;
            }
            return f1.getTime().compareTo(f2.getTime()) ? 1 : -1;
        });
        FlightMap = new HashMap<>(flightList.size());
        System.out.println("creating map..");
        for (Flight lflight: flightList) {
            //System.out.println(lflight);
            if(FlightMap.containsKey(lflight.getTime())) {
                value = FlightMap.get(lflight.getTime());
                FlightMap.replace(lflight.getTime(), value+lflight.getSeats());
                if(value == FlightMap.get(lflight.getTime()))
                    System.out.println("value didn't change");
            }
            else
                FlightMap.put(lflight.getTime(), lflight.getSeats());
        }
        int flights = 0;
        for(Map.Entry<Time, Integer> entry: FlightMap.entrySet()) {
            //System.out.println(entry.getKey() + "  " + entry.getValue());
            flights += entry.getValue();
        }
        System.out.println("map size:" + FlightMap.size());
        System.out.println("seats num:" + flights);
        //System.out.println(new Time(0, 20).DifferenceMin(20, 20)/60);

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            System.out.print("Couldn't close Workbook");
            e.printStackTrace();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime dateTime1= LocalDateTime.parse("2014-11-25 19:00:00", formatter);
        LocalDateTime dateTime2= LocalDateTime.parse("2014-11-26 00:30:00", formatter);

        long diffInMilli = java.time.Duration.between(dateTime1, dateTime2).toMillis();
        long diffInSeconds = java.time.Duration.between(dateTime1, dateTime2).getSeconds();
        long diffInMinutes = java.time.Duration.between(dateTime1, dateTime2).toMinutes();
        long diffInHours = java.time.Duration.between(dateTime1, dateTime2).toHours();
        System.out.println("Hours: " + diffInHours);
        Time time1 = new Time(20, 40);
        System.out.println(time1.DifferenceMin(22, 46));
        return FlightMap;
    }
}
