package com.company;

import com.google.common.math.Stats;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.util.IntRange;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class GA {
    private static final int tournamentSampleSize = 5;
    // Number of best individuals preserved for next generation: elites
    private static final int eliteCount = 1;
    private static final int populationSize = 200;
    private static final int runtime = 10; //seconds;
    //Hours Range
    private static final IntRange hRange = IntRange.of(8, 18);
    //Minutes Range (sliced in pieces of 5 minutes) ((number * 5) to get actual minutes) look at **
    private static final IntRange mRange = IntRange.of(0, 11);
    //work Hours
    private static final IntRange dRange = IntRange.of(4, 4);
    //specific Date for GA search
    //static LocalDate ld;
    //part of the whole list that contains flights for specific GA search
    static FlightList flightList;
    static Map<LocalTime, Integer> timesMap;
    private static List<Solution> solutionList;

    static Double getFitness(final Genotype gt) {
        double fitness = 0;
        int hours, min, d;
        hours = ((NumericGene) gt.get(0, 0)).intValue();
        min = ((NumericGene) gt.get(1, 0)).intValue() * 5;// ** here
        d = ((NumericGene) gt.get(2, 0)).intValue();
        for(Map.Entry<LocalTime, Integer> entry: timesMap.entrySet()) {
            if(minDif(entry.getKey(), hours, min, true)<=(d*60)) {
                //System.out.println(entry.getKey().DifferenceMin(hours, min));
                fitness += entry.getValue();
            }
        }
        //fitness = (int)( ((double)fitness)/((d*192541)/24)*100);
        //return fitness;
        //System.out.println("[[[" + hours + "],[" + min + "]" + d +"]] --> " + fitness/d);
        return fitness/d;
    }

    static Double getFitnessByDays(final Genotype gt) {
        double fitness = 0;
        int hour, min, d;
        hour = ((NumericGene) gt.get(0, 0)).intValue();
        min = ((NumericGene) gt.get(1, 0)).intValue() * 5;//here **
        d = ((NumericGene) gt.get(2, 0)).intValue();
        LocalTime time = LocalTime.of(hour, min);
        for(Flight flight: flightList) {
            if(minDif(flight.getTime(), time, true)<=(d*60)) {
                //System.out.println(entry.getKey().DifferenceMin(hours, min));
                fitness += flight.getSeats();
            }
        }
        //fitness = (int)( ((double)fitness)/((d*192541)/24)*100);
        //return fitness;
        //System.out.println("[[[" + hours + "],[" + min + "]" + d +"]] --> " + fitness/d);
        //returned fitness is totalSeats/d to get seats per hour
        return fitness/d;
    }
    static Double getFitnessByMean(final Genotype gt) {
        double fitness = 0;
        int hour, min, d;
        hour = ((NumericGene) gt.get(0, 0)).intValue();
        min = ((NumericGene) gt.get(1, 0)).intValue() * 5;//here **
        d = ((NumericGene) gt.get(2, 0)).intValue();
        LocalTime time = LocalTime.of(hour, min);
        for(Flight flight: flightList) {
            if(minDif(flight.getTime(), time, true)<=(d*60)) {
                //System.out.println(entry.getKey().DifferenceMin(hours, min));
                fitness += flight.getSeats();
            }
        }
        //fitness = (int)( ((double)fitness)/((d*192541)/24)*100);
        //return fitness;
        //System.out.println("[[[" + hours + "],[" + min + "]" + d +"]] --> " + fitness/d);
        int[] deviations = solutionList.stream().mapToInt(sol -> GA.shortestMinDif(sol.getTime(), time)).toArray();
        Stats devStats = Stats.of(deviations);
        //System.out.println(devStats.populationStandardDeviation());
        //returned fitness is totalSeats/d to get seats per hour
        return (fitness/d)/(devStats.populationStandardDeviation()*devStats.mean());
    }


    static Phenotype run(Function<Genotype<IntegerGene>, Double> ff) {
        Genotype<IntegerGene> gt = Genotype.of(
                IntegerChromosome.of(IntegerGene.of(hRange)),
                IntegerChromosome.of(IntegerGene.of(mRange)),
                IntegerChromosome.of(IntegerGene.of(dRange))
        );
        final Selector<IntegerGene, Double> selector = new EliteSelector<IntegerGene, Double>(
                eliteCount, new TournamentSelector<>(tournamentSampleSize));
        Engine<IntegerGene, Double> engine
                = Engine.builder(ff, gt)
                //.populationSize(populationSize)
                .optimize(Optimize.MAXIMUM)
                //.alterers(new Mutator<>(0.25), new MeanAlterer<>(0.23))
                //.selector(selector)
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
        final Phenotype best = engine.stream()
                //.limit(Limits.byFitnessThreshold (ExcelReader.totalSeats))
                //.limit(Limits.byExecutionTime ( Duration.ofSeconds(runtime)))
                .limit(Limits.bySteadyFitness(1000))
                //.peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());
        //System.out.println(statistics);
        //System.out.println(best);
        return best;
    }

    static Phenotype PhenotypeByMean() {
        solutionList = new ArrayList<>(31);
        for (int i = 1; i <= 31; i++) {
            System.out.print(i + " of " + 31 + " ");
            GA.flightList = GA.flightList(LocalDate.of(2018, 7, i), null);
            solutionList.add(new Solution(run(GA::getFitnessByDays)));
            System.out.println(solutionList.get(i-1));
        }
        GA.flightList = ExcelReader.flightList;
        //GA.timesMap = ExcelReader.TimesMap(GA.flightList);
        //System.out.println("Running GA");
        return run(GA::getFitnessByMean);
    }

    //These minutesDifference methods return minutes to be elapsed from startTime to endTime choosing clock indexes' direction, right for true and left for false
    private static int minDif(LocalTime startTime, int hour, int min, boolean direction) {
        return minDif(startTime, LocalTime.of(hour, min), direction);
    }

    private static int minDif(LocalTime startTime, LocalTime endTime, boolean direction) {
        int sMins = toMins(startTime);
        int eMins = toMins(endTime);
        if (!direction) {
            int temp = sMins;
            sMins = eMins;
            eMins = temp;
        }
        if(eMins<sMins) {
            return ((24 * 60) - sMins) + eMins;
        }
        else
            return eMins - sMins;
    }

    private static int toMins(LocalTime time) {
        return (time.getHour() * 60) + time.getMinute();
    }

    private static int shortestMinDif(LocalTime startTime, LocalTime endTime) {
        int dif1 = minDif(startTime, endTime, true);
        int dif2 = minDif(startTime, endTime, false);
        return Math.min(dif1, dif2);
    }

    //List of flights under some restrictions
    private static FlightList flightList(LocalDate sDate, LocalDate eDate){
        FlightList list = new FlightList();
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
