package com.company;

import javafx.application.Application;
import javafx.application.Platform;

public class Main {
    public static void main(String args[]) {
        ExcelReader.run();
        Application.launch(FlightChart.class);
        GA.run();
    }
}
