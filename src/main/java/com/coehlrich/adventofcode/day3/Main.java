package com.coehlrich.adventofcode.day3;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Point2;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Symbol[][] symbols = input.lines().map(line -> {
            Symbol[] row = new Symbol[line.length()];
            char[] charArray = line.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char character = charArray[i];
                if (!Character.isDigit(character) && character != '.') {
                    row[i] = character == '*' ? Symbol.GEAR : Symbol.OTHER;
                } else {
                    row[i] = Symbol.NOTHING;
                }
            }
            return row;
        }).toArray(Symbol[][]::new);
//        for (int y = 0; y < symbols.length; y++) {
//            for (int x = 0; x < symbols[y].length; x++) {
//                System.out.print(symbols[y][x] ? 'x' : '.');
//            }
//            System.out.println();
//        }

        Map<Point2, IntList> gears = new HashMap<>();
        char[][] map = input.lines().map(String::toCharArray).toArray(char[][]::new);
        int part1 = 0;
        for (int y = 0; y < map.length; y++) {
            int value = 0;
            boolean valid = false;
            Set<Point2> foundGears = new HashSet<>();
            for (int x = 0; x < map[y].length; x++) {
                char character = map[y][x];
                if (Character.isDigit(character)) {
                    value *= 10;
                    value += Character.digit(character, 10);
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (y + dy >= 0 && y + dy < map.length && x + dx >= 0 && x + dx < map[y].length) {
                                valid |= symbols[y + dy][x + dx] != Symbol.NOTHING;
                                if (symbols[y + dy][x + dx] == Symbol.GEAR) {
                                    Point2 pos = new Point2(x + dx, y + dy);
                                    foundGears.add(pos);
                                }
                            }
                        }
                    }
                } else {
                    if (valid) {
                        part1 += value;
                        for (Point2 gear : foundGears) {
                            IntList parts = gears.computeIfAbsent(gear, pos -> new IntArrayList());
                            parts.add(value);
                        }
//                        System.out.print(value);
                    }
                    while (value != 0 && !valid) {
//                        System.out.print('x');
                        value /= 10;
                    }
//                    System.out.print(symbols[y][x] != Symbol.NOTHING ? '#' : '.');
                    value = 0;
                    valid = false;
                    foundGears = new HashSet<>();
                }
            }
            if (valid) {
                part1 += value;
                for (Point2 gear : foundGears) {
                    IntList parts = gears.computeIfAbsent(gear, pos -> new IntArrayList());
                    parts.add(value);
                }
//                System.out.print(value);
            }
//            System.out.println();
        }
        return new Result(part1, gears.values().stream().mapToInt(parts -> parts.size() == 2 ? parts.getInt(0) * parts.getInt(1) : 0).sum());
    }

}
