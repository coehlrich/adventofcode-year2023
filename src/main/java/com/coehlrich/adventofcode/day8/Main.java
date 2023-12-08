package com.coehlrich.adventofcode.day8;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        String[] sections = input.split("\n\n");
        Map<String, Area> areas = sections[1].lines()
                .map(Area::parse)
                .collect(Collectors.toMap(Area::name, Function.identity()));
        String instructions = sections[0];

        return new Result(
                calculate(areas, List.of(areas.get("AAA")), "ZZZ", instructions),
                calculate(areas, areas.values().stream().filter(area -> area.name().endsWith("A")).toList(), "Z", instructions));
    }

    private long calculate(Map<String, Area> areas, List<Area> start, String end, String instructions) {
        int result = 0;
        LongList required = new LongArrayList();
        List<Area> rooms = start;
        for (; !rooms.isEmpty(); result++) {
            char instruction = instructions.charAt(result % instructions.length());
            rooms = rooms.stream().map(area -> areas.get(switch (instruction) {
                case 'L' -> area.left();
                case 'R' -> area.right();
                default -> throw new IllegalArgumentException("Unexpected value: " + instruction);
            })).toList();

            for (Area area : rooms) {
                if (area.name().endsWith(end)) {
                    required.add(result + 1);
                }
            }
            rooms = rooms.stream().filter(area -> !area.name().endsWith(end)).toList();
//            rooms = switch (instruction) {
//                case 'L' -> areas.get(rooms).left();
//                case 'R' -> areas.get(rooms).right();
//                default -> throw new IllegalArgumentException("Unexpected value: " + instruction);
//            };
        }
        long returnValue = required.longStream().reduce(1, this::lcm);
        return returnValue;
    }

    private long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    private long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

}
