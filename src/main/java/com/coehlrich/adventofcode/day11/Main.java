package com.coehlrich.adventofcode.day11;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Point2;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        String[] lines = input.lines().toArray(String[]::new);

        return new Result(calculate(lines, false), calculate(lines, true));
    }

    public long calculate(String[] lines, boolean part2) {
        Set<Point2> galaxies = new HashSet<>();
        int addY = 0;
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            if (!line.contains("#")) {
                addY += part2 ? 999999 : 1;
            } else {
                int addX = 0;
                for (int x = 0; x < line.length(); x++) {
                    int finalX = x;
                    if (Stream.of(lines).allMatch(string -> string.charAt(finalX) == '.')) {
                        addX += part2 ? 999999 : 1;
                    } else if (line.charAt(x) == '#') {
                        galaxies.add(new Point2(x + addX, y + addY));
                    }
                }
            }
        }
        return galaxies.stream()
                .mapToLong(galaxy1 -> galaxies.stream()
                        .mapToLong(galaxy1::distance)
                        .sum())
                .sum() / 2;
    }

}
