package com.coehlrich.adventofcode.day16;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;

import java.util.*;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Tile[][] map = input.lines()
                .map(line -> line.chars()
                        .mapToObj(character -> switch (character) {
                            case '.' -> new Empty();
                            case '/' -> new Mirror(false);
                            case '\\' -> new Mirror(true);
                            case '|' -> new Splitter(false);
                            case '-' -> new Splitter(true);
                            default -> throw new IllegalArgumentException("Unexpected value: " + character);
                        })
                        .toArray(Tile[]::new))
                .toArray(Tile[][]::new);

        long part2 = 0;
        for (int y = 0; y < map.length; y++) {
            part2 = Math.max(part2, calculate(new Beam(new Point2(0, y), Direction.RIGHT), map));
            part2 = Math.max(part2, calculate(new Beam(new Point2(map[0].length - 1, y), Direction.LEFT), map));
        }

        for (int x = 0; x < map[0].length; x++) {
            part2 = Math.max(part2, calculate(new Beam(new Point2(x, 0), Direction.DOWN), map));
            part2 = Math.max(part2, calculate(new Beam(new Point2(x, map.length - 1), Direction.UP), map));
        }

        return new Result(calculate(new Beam(new Point2(0, 0), Direction.RIGHT), map), part2);
    }

    private long calculate(Beam start, Tile[][] map) {
        Set<Beam> beams = new HashSet<>();
        Queue<Beam> queue = new LinkedList<>();
        beams.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {
            Beam beam = queue.poll();
            Tile tile = map[beam.pos().y()][beam.pos().x()];
            List<Direction> directions = tile.beam(beam.dir());
            for (Direction dir : directions) {
                Point2 pos = dir.offset(beam.pos());
                Beam newBeam = new Beam(pos, dir);
                if (newBeam.pos().y() >= 0 && newBeam.pos().y() < map.length && newBeam.pos().x() >= 0 && newBeam.pos().x() < map[0].length) {
                    if (beams.add(newBeam)) {
                        queue.add(newBeam);
                    }
                }
            }
        }
        return beams.stream().map(Beam::pos).distinct().count();
    }

}
