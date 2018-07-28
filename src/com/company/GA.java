package com.company;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.util.IntRange;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;

class GA {
    private static final int tournamentSampleSize = 5;
    private static final int populationSize = 200;
    private static final int runtime = 10; //seconds;
    //Hours Range
    private static final IntRange hRange = IntRange.of(6, 10);
    //Minutes Range (sliced in pieces of 5 minutes) ((number * 5) to get minutes) look at **
    private static final IntRange mRange = IntRange.of(0, 11);
    //work Hours
    private static final IntRange dRange = IntRange.of(4, 8);

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

    static void run() {
        Genotype<IntegerGene> gt = Genotype.of(
                IntegerChromosome.of(IntegerGene.of(hRange)),
                IntegerChromosome.of(IntegerGene.of(mRange)),
                IntegerChromosome.of(IntegerGene.of(dRange))
        );
        final Selector<IntegerGene, Integer> selector = new EliteSelector<IntegerGene, Integer>(
                // Number of best individuals preserved for next generation: elites
                1, new TournamentSelector<>(tournamentSampleSize));
        Engine<IntegerGene, Integer> engine
                = Engine.builder(GA::getFitness, gt)
                .populationSize(populationSize)
                .optimize(Optimize.MAXIMUM)
                .alterers(new Mutator<>(0.05), new MeanAlterer<>(0.03))
                .selector(selector)
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
        final Phenotype best = engine.stream()
                //.limit(Limits.byFitnessThreshold (ExcelReader.totalSeats))
                .limit(Limits.byExecutionTime ( Duration.ofSeconds(runtime)))
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());
        System.out.println(statistics);
        System.out.println(best);
        System.out.println(192541);
    }

    private static int minDif(LocalTime time, int hours, int mins) {
        int tMins = (time.getHour()*60) + time.getMinute();
        mins += hours*60;
        if(mins<tMins) {
            return ((24 * 60) - tMins) + mins;
        }
        else
            return mins - tMins;
    }
}
