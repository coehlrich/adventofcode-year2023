package com.coehlrich.adventofcode.day5;

import java.util.ArrayList;
import java.util.List;

public record Range(long start, long length) {
    public static List<Range> parseSeeds(String line) {
        List<Range> ranges = new ArrayList<>();
        String[] seedStrings = line.replace("seeds: ", "").split(" ");
        for (int i = 0; i < seedStrings.length; i += 2) {
            ranges.add(new Range(Long.parseLong(seedStrings[i]), Long.parseLong(seedStrings[i + 1])));
        }
        return ranges;
    }
}
