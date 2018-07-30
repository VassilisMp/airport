package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.Map;

public class FlightChart extends Application {

    @Override public void start(Stage stage) {
        stage.setTitle("Line Chart Flights");
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Hour");
        //creating the chart
        final LineChart<String, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Mykonos July Flights");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Number of flights");
        //populating the series with data
        boolean has;
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 5) {
                has = false;
                LocalTime time = LocalTime.of(i, j);
                for (Map.Entry<LocalTime, Integer> entry : GA.timesMap.entrySet()) {
                    if (entry.getKey().equals(time)) {
                        //System.out.println(entry.getValue());
                        //noinspection unchecked
                        series.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
                        has = true;
                    }
                }
                if (!has)
                    //noinspection unchecked
                    series.getData().add(new XYChart.Data(time.toString(), 0));
            }
        }

        Scene scene = new Scene(lineChart, 800, 600);
        //noinspection unchecked
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
        /*try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stage.close();*/
    }
}
