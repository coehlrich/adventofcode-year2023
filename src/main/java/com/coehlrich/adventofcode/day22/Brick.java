package com.coehlrich.adventofcode.day22;

import com.coehlrich.adventofcode.util.Point3;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Brick(Point3 start, Point3 end) {

    private static final Pattern PATTERN = Pattern.compile("(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)");

    public static Brick parse(String line) {
        Matcher matcher = PATTERN.matcher(line);
        matcher.matches();
        int ax = Integer.parseInt(matcher.group(1));
        int ay = Integer.parseInt(matcher.group(2));
        int az = Integer.parseInt(matcher.group(3));
        int bx = Integer.parseInt(matcher.group(4));
        int by = Integer.parseInt(matcher.group(5));
        int bz = Integer.parseInt(matcher.group(6));

        return new Brick(new Point3(ax, ay, az), new Point3(bx, by, bz));
    }

    public List<Point3> getTiles() {
        List<Point3> tiles = new ArrayList<>();
        for (int x = start.x(); x <= end.x(); x++) {
            for (int y = start.y(); y <= end.y(); y++) {
                for (int z = start.z(); z <= end.z(); z++) {
                    tiles.add(new Point3(x, y, z));
                }
            }
        }
        return tiles;
    }
}
