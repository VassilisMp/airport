package com.company;

import java.util.Objects;

public class Time {
    private int hours;
    private int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return hours == time.hours &&
                minutes == time.minutes;
    }

    public boolean compareTo(Time o) {
        return (this.hours > o.hours) || (this.hours == o.hours && this.minutes > o.minutes);
    }

    public int DifferenceMin(Time o) {
        return (Math.abs(this.hours-o.hours)*60)+Math.abs(this.minutes-o.minutes);
    }
    public int DifferenceMin(int hours, int min) {
        int thisMins = this.inMinutes();
        int mins = (hours*60) + min;
        if(mins<thisMins) {
            return ((24 * 60) - thisMins) + mins;
        }
        else
            return mins - thisMins;
    }
    private int inMinutes() {
        return (this.hours*60) + this.minutes;
    }

    @Override
    public int hashCode() {

        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString() {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
    }
}
