package com.coehlrich.adventofcode.day6;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import java.util.ArrayList;
import java.util.List;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        String[][] lines = input.lines().map(line -> line.split("\\s+")).toArray(String[][]::new);
        List<Race> races = new ArrayList<>();
        for (int i = 1; i < lines[0].length; i++) {
            races.add(new Race(Long.parseLong(lines[0][i]), Long.parseLong(lines[1][i])));
        }

        String[] linesArray = input.lines().toArray(String[]::new);
        long singleTime = Long.parseLong(linesArray[0].replace(" ", "").replace("Time:", ""));
        long singleDistance = Long.parseLong(linesArray[1].replace(" ", "").replace("Distance:", ""));
        return new Result(race(races), race(List.of(new Race(singleTime, singleDistance))));
    }

    private int race(List<Race> races) {
        int wins = 1;
        for (Race race : races) {
            int count = 0;
            long raceTime = race.time();
            for (long i = 1; i < raceTime; i++) {
                if (i * (raceTime - i) > race.distance()) {
                    count++;
                }
            }
            wins *= count;
        }
        return wins;
    }

}
