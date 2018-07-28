package com.company;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;

public class LineChartSample extends Application {
    public static final int tournamentSampleSize = 5;
    public static final int populationSize = 200;
    private static final int runtime = 10; //seconds;
    //Hours Range
    final IntRange hRange = IntRange.of(6, 10);
    //Minutes Range (sliced in pieces of 5 minutes) ((number * 5) to get minutes) look at **
    final IntRange mRange = IntRange.of(0, 11);
    //work Hours
    final IntRange dRange = IntRange.of(4, 8);

    private static Integer getFitness(final Genotype gt) {
        int fitness = 0;
        int hours, min, d;
        hours = ((NumericGene) gt.get(0, 0)).intValue();
        min = ((NumericGene) gt.get(1, 0)).intValue() * 5;//here **
        d = ((NumericGene) gt.get(2, 0)).intValue();
        for(Map.Entry<LocalTime, Integer> entry: ExcelReader.FlightMap.entrySet()) {
            if(minDif(entry.getKey(), hours, min)<=(d*60)) {
                //System.out.println(entry.getKey().DifferenceMin(hours, min));
                fitness += entry.getValue();
            }
        }
        //fitness = (int)( ((double)fitness)/((d*192541)/24)*100);
        //return fitness;
        //System.out.println("[[[" + hours + "],[" + min + "]" + d +"]] --> " + fitness);
        return fitness/d;
    }
    @Override public void start(Stage stage) {
        ExcelReader.run();
        stage.setTitle("Line Chart Flights");
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Hour");
        //creating the chart
        final LineChart<String,Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Mykonos July Flights");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Number of flights");
        //populating the series with data
        /*for(Map.Entry<Time, Integer> entry: excelReader.FlightMap.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
        }*/
        boolean has = false;
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j+=5) {
                has = false;
                LocalTime time = LocalTime.of(i, j);
                for(Map.Entry<LocalTime, Integer> entry: ExcelReader.FlightMap.entrySet()) {
                    if(entry.getKey().equals(time)) {
                        //System.out.println(entry.getValue());
                        series.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
                        has = true;
                    }
                }
                if(!has)
                    series.getData().add(new XYChart.Data(time.toString(), 0));
            }
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
        Genotype<IntegerGene> gt = Genotype.of(
                IntegerChromosome.of(IntegerGene.of(hRange)),
                IntegerChromosome.of(IntegerGene.of(mRange)),
                IntegerChromosome.of(IntegerGene.of(dRange))
        );
        final Selector<IntegerGene, Integer> selector = new EliteSelector<IntegerGene, Integer>(
                // Number of best individuals preserved for next generation: elites
                1, new TournamentSelector<>(tournamentSampleSize));
        Engine<IntegerGene, Integer> engine
                = Engine.builder(LineChartSample::getFitness, gt)
                .populationSize(populationSize)
                .optimize(Optimize.MAXIMUM)
                .alterers(new Mutator<>(0.05), new MeanAlterer<>(0.03))
                .selector(selector)
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
        final Phenotype best = engine.stream()
                .limit(Limits.byFitnessThreshold (192541))
                .limit(Limits.byExecutionTime ( Duration.ofSeconds(runtime)))
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());
        System.out.println(statistics);
        System.out.println(best);
        System.out.println(192541);
    }

    public static int minDif(LocalTime time, int hours, int mins) {
        int tMins = (time.getHour()*60) + time.getMinute();
        mins += hours*60;
        if(mins<tMins) {
            return ((24 * 60) - tMins) + mins;
        }
        else
            return mins - tMins;
    }
}
