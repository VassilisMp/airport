package com.company;

import io.jenetics.NumericGene;
import io.jenetics.Phenotype;

import java.time.LocalTime;

class Solution {
    private LocalTime time;
    private int duration;
    private int fitness;
    private int totalSeats;
    private int totalFlights;

    Solution(LocalTime time, int duration, int fitness) {
        this.time = time;
        this.duration = duration;
        this.fitness = fitness;
    }

    public Solution(Phenotype ph) {
        this.totalFlights = 0;
        int hour = ((NumericGene) ph.getGenotype().get(0, 0)).intValue();
        int min = ((NumericGene) ph.getGenotype().get(1, 0)).intValue() * 5;
        this.duration = ((NumericGene) ph.getGenotype().get(2, 0)).intValue();
        this.fitness = GA.getFitnessByDays(ph.getGenotype());
        this.time = LocalTime.of(hour, min);
        this.totalSeats = this.fitness * this.duration;
        for (Flight flight: ExcelReader.flightList)
            if(GA.minDif(flight.getTime(), time)<=(duration*60))
                this.totalFlights++;
    }

    LocalTime getTime() {
        return time;
    }

    int getDuration() {
        return duration;
    }

    int getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "time=" + time + " - " + time.plusHours(duration) +
                ", duration=" + duration +
                ", fitness=" + fitness +
                ", totalSeats=" + totalSeats +
                ", totalFlights=" + totalFlights +
                '}';
    }
}
