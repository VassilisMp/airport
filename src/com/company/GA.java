package com.company;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.util.IntRange;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class GA {
    private static final int tournamentSampleSize = 5;
    // Number of best individuals preserved for next generation: elites
    private static final int eliteCount = 3;
    private static final int populationSize = 200;
    private static final int runtime = 10; //seconds;
    //Hours Range
    private static final IntRange hRange = IntRange.of(8, 18);
    //Minutes Range (sliced in pieces of 5 minutes) ((number * 5) to get minutes) look at **
    private static final IntRange mRange = IntRange.of(0, 11);
    //work Hours
    private static final IntRange dRange = IntRange.of(4, 4);
    //specific Date for GA search
    //static LocalDate ld;
    //part of the whole list that contains flights for specific GA search
    static List<Flight> flightList;
    static Map<LocalTime, Integer> timesMap;

    static Integer getFitness(final Genotype gt) {
        int fitness = 0;
        int hours, min, d;
        hours = ((NumericGene) gt.get(0, 0)).intValue();
        min = ((NumericGene) gt.get(1, 0)).intValue() * 5;//here **
        d = ((NumericGene) gt.get(2, 0)).intValue();
        for(Map.Entry<LocalTime, Integer> entry: timesMap.entrySet()) {
            if(minDif(entry.getKey(), hours, min)<=(d*60)) {
                //System.out.println(entry.getKey().DifferenceMin(hours, min));
                fitness += entry.getValue();
            }
        }
        //fitness = (int)( ((double)fitness)/((d*192541)/24)*100);
        //return fitness;
        //System.out.println("[[[" + hours + "],[" + min + "]" + d +"]] --> " + fitness/d);
        return fitness/d;
    }

    static Integer getFitnessByDays(final Genotype gt) {
        int fitness = 0;
        int hours, min, d;
        hours = ((NumericGene) gt.get(0, 0)).intValue();
        min = ((NumericGene) gt.get(1, 0)).intValue() * 5;//here **
        d = ((NumericGene) gt.get(2, 0)).intValue();
        for(Flight flight: flightList) {
            if(minDif(flight.getTime(), hours, min)<=(d*60)) {
                //System.out.println(entry.getKey().DifferenceMin(hours, min));
                fitness += flight.getSeats();
            }
        }
        //fitness = (int)( ((double)fitness)/((d*192541)/24)*100);
        //return fitness;
        //System.out.println("[[[" + hours + "],[" + min + "]" + d +"]] --> " + fitness/d);
        return fitness/d;
    }

    static int[] run(Function<Genotype<IntegerGene>, Integer> ff) {
        int array[] = new int[4];
        Genotype<IntegerGene> gt = Genotype.of(
                IntegerChromosome.of(IntegerGene.of(hRange)),
                IntegerChromosome.of(IntegerGene.of(mRange)),
                IntegerChromosome.of(IntegerGene.of(dRange))
        );
        final Selector<IntegerGene, Integer> selector = new EliteSelector<IntegerGene, Integer>(
                eliteCount, new TournamentSelector<>(tournamentSampleSize));
        Engine<IntegerGene, Integer> engine
                = Engine.builder(ff, gt)
                //.populationSize(populationSize)
                .optimize(Optimize.MAXIMUM)
                //.alterers(new Mutator<>(0.25), new MeanAlterer<>(0.23))
                //.selector(selector)
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
        final Phenotype best = engine.stream()
                //.limit(Limits.byFitnessThreshold (ExcelReader.totalSeats))
                .limit(Limits.byExecutionTime ( Duration.ofSeconds(runtime)))
                .limit(Limits.bySteadyFitness(1000))
                //.peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());
        //System.out.println(statistics);
        System.out.println(best);
        //hours
        array[0] = ((NumericGene) best.getGenotype().get(0, 0)).intValue();
        //mins
        array[1] = ((NumericGene) best.getGenotype().get(1, 0)).intValue() * 5;
        //duration
        array[2] = ((NumericGene) best.getGenotype().get(2, 0)).intValue();
        //fitness
        array[3] = getFitnessByDays(best.getGenotype());
        return array;
    }

    static int minDif(LocalTime time, int hours, int mins) {
        int tMins = (time.getHour()*60) + time.getMinute();
        mins += hours*60;
        if(mins<tMins) {
            return ((24 * 60) - tMins) + mins;
        }
        else
            return mins - tMins;
    }

    static List<Flight> flightList (LocalDate sDate, LocalDate eDate){
        List<Flight> list = new ArrayList<>();
        if (eDate == null)
            for(Flight flight: ExcelReader.flightList) {
                if(flight.getDate().isEqual(sDate))
                    list.add(flight);
            }
        else
            for(Flight flight: ExcelReader.flightList) {
                if(flight.getDate().isEqual(sDate)
                        || flight.getDate().isEqual(eDate)
                        || (flight.getDate().isAfter(sDate) && flight.getDate().isBefore(eDate)) )
                    list.add(flight);
            }
        return list;
    }
}
