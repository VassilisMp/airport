package com.company;

import java.time.LocalTime;

class Solution {
    private LocalTime time;
    private int duration;
    private int fitness;

    public Solution(LocalTime time, int duration, int fitness) {
        this.time = time;
        this.duration = duration;
        this.fitness = fitness;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public int getFitness() {
        return fitness;
    }
}
