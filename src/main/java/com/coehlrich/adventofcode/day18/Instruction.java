package com.coehlrich.adventofcode.day18;

import com.coehlrich.adventofcode.util.Direction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Instruction(Dig part1, Dig part2) {

    private static final Pattern PATTERN = Pattern.compile("(U|D|L|R) (\\d+) \\(#([0-9a-f]{5})([0-3]{1})\\)");

    public static Instruction parse(String line) {
        Matcher matcher = PATTERN.matcher(line);
        matcher.matches();

        Direction dir = switch (matcher.group(1).charAt(0)) {
            case 'U' -> Direction.UP;
            case 'D' -> Direction.DOWN;
            case 'L' -> Direction.LEFT;
            case 'R' -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Unexpected value: " + matcher.group(1).charAt(0));
        };
        int steps = Integer.parseInt(matcher.group(2));
        int steps2 = Integer.parseUnsignedInt(matcher.group(3), 16);
        Direction dir2 = switch (matcher.group(4).charAt(0)) {
            case '0' -> Direction.RIGHT;
            case '1' -> Direction.DOWN;
            case '2' -> Direction.LEFT;
            case '3' -> Direction.UP;
            default -> throw new IllegalArgumentException("Unexpected value: " + matcher.group(4).charAt(0));
        };
        return new Instruction(new Dig(dir, steps), new Dig(dir2, steps2));
    }

}
