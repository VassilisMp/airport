package com.company;

import io.jenetics.NumericGene;
import io.jenetics.Phenotype;

import java.time.LocalTime;

class Solution {
    private LocalTime time;
    private int duration;
    private double fitness;
    private int totalSeats;

    public Solution(Phenotype ph) {
        int hour = ((NumericGene) ph.getGenotype().get(0, 0)).intValue();
        int min = ((NumericGene) ph.getGenotype().get(1, 0)).intValue() * 5;
        this.duration = ((NumericGene) ph.getGenotype().get(2, 0)).intValue();
        this.fitness = GA.getFitnessByDays(ph.getGenotype());
        this.time = LocalTime.of(hour, min);
        this.totalSeats = GA.getFitnessByDays(ph.getGenotype()).intValue() * this.duration;
    }

    LocalTime getTime() {
        return time;
    }

    int getDuration() {
        return duration;
    }

    double getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "time=" + time + " - " + time.plusHours(duration) +
                ", duration=" + duration +
                ", fitness=" + fitness +
                ", totalSeats=" + totalSeats +
                '}';
    }
}
