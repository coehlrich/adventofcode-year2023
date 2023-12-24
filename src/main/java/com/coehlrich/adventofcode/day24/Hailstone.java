package com.coehlrich.adventofcode.day24;

import com.coehlrich.adventofcode.util.Point3;
import com.coehlrich.adventofcode.util.Point3Long;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Hailstone(Point3Long pos, Point3 speed) {

    private static final Pattern PATTERN = Pattern.compile("(\\d+), (\\d+), (\\d+) @ (-?\\d+), (-?\\d+), (-?\\d+)");

    public static Hailstone parse(String line) {
        Matcher matcher = PATTERN.matcher(line);
        matcher.matches();

        long x = Long.parseLong(matcher.group(1));
        long y = Long.parseLong(matcher.group(2));
        long z = Long.parseLong(matcher.group(3));
        int dx = Integer.parseInt(matcher.group(4));
        int dy = Integer.parseInt(matcher.group(5));
        int dz = Integer.parseInt(matcher.group(6));

        return new Hailstone(new Point3Long(x, y, z), new Point3(dx, dy, dz));
    }

}
