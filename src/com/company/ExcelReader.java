package com.company;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ExcelReader {
    private static final String XLSX_FILE_PATH = "./Multi-Day Flightplan.xlsx";
    //some variables of this class get values after running run() method, others from else
    static List<Flight> flightList;
    static int diffTimesNum;

    static void run() {

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
        flightList = new FlightList();
        System.out.println("Creating list..");
        for (Row row: sheet) {
            DataFormatter dataFormatter = new DataFormatter();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyyHH:mm");
            //date retrieve
            Cell cell = row.getCell(1);
            String date = dataFormatter.formatCellValue(cell);
            if (date.equals(""))
                break;
            //STA retrieve
            cell = row.getCell(9);
            String STA = dataFormatter.formatCellValue(cell);
            if (STA.equals("") || STA.equals("STA"))
                continue;
            //seats retrieve
            cell = row.getCell(11);
            int seats = (int)cell.getNumericCellValue();
            if (seats<=0)
                continue;
            //day retrieve
            cell = row.getCell(2);
            String Day = dataFormatter.formatCellValue(cell); //################################# I must make String Day to Enum DayOfWeek
            DayOfWeek eDay = DayStrToEnum(Day);
            if (eDay==null) {
                System.out.println("String Day to DayOfWeek Enum error");
                continue;
            }
            //System.out.println(Day);
            //System.out.println(date);
            LocalDateTime dateTime = LocalDateTime.parse(date + STA, formatter);
            flightList.add(new Flight(dateTime, eDay, seats));
        }
        /*System.out.println("sorting list..");
        flightList.sort((f1, f2) -> {
            if(f1.getTime().equals(f2.getTime())){
                return 0;
            }
            return f1.getTime().compareTo(f2.getTime());
        });*/

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
        System.out.println("Hours: " + diffInHours);*/
    }

    static Map<LocalTime, Integer> TimesMap(List<Flight> flightList) {
        int value;
        Map<LocalTime, Integer> timesMap = new HashMap<>(flightList.size());
        System.out.println("creating map..");
        for (Flight flight: flightList) {
            //System.out.println(flight);
            if(timesMap.containsKey(flight.getTime())) {
                value = timesMap.get(flight.getTime());
                timesMap.replace(flight.getTime(), value+flight.getSeats());
                /*if(value == timesMap.get(flight.getTime()))
                    System.out.println("value didn't change");*/
            }
            else
                timesMap.put(flight.getTime(), flight.getSeats());
        }
        diffTimesNum = timesMap.size();
        /*for(Map.Entry<LocalTime, Integer> entry: timesMap.entrySet()) {
            System.out.println(entry.getKey() + "  " + entry.getValue());
        }*/
        //System.out.println("map size:" + timesMap.size());
        //System.out.println("seats num:" + totalSeats);
        return timesMap;
    }

    static DayOfWeek DayStrToEnum (String day) {
        switch (day) {
            case "SUN": return DayOfWeek.SUNDAY;
            case "MON": return DayOfWeek.MONDAY;
            case "TUE": return DayOfWeek.TUESDAY;
            case "WED": return DayOfWeek.WEDNESDAY;
            case "THU": return DayOfWeek.THURSDAY;
            case "FRI": return DayOfWeek.FRIDAY;
            case "SAT": return DayOfWeek.SATURDAY;
            default: return null;
        }
    }
}
