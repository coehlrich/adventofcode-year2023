package com.coehlrich.adventofcode.day10;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Queue;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Map<Point2, Type> tiles = new HashMap<>();
        String[] lines = input.lines().toArray(String[]::new);
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                tiles.put(new Point2(x, y), switch (line.charAt(x)) {
                    case '|' -> Type.VERTICAL;
                    case '-' -> Type.HORIZONTAL;
                    case 'L' -> Type.NORTH_EAST;
                    case 'J' -> Type.NORTH_WEST;
                    case '7' -> Type.SOUTH_WEST;
                    case 'F' -> Type.SOUTH_EAST;
                    case '.' -> Type.GROUND;
                    case 'S' -> Type.STARTING;
                    default -> throw new IllegalArgumentException("Unexpected value: " + line.charAt(x));
                });
            }
        }

        Point2 starting = tiles.entrySet().stream()
                .filter(entry -> entry.getValue() == Type.STARTING)
                .map(Map.Entry::getKey)
                .findFirst().get();

        Object2IntMap<Point2> distances = new Object2IntOpenHashMap<>();

        Queue<State> queue = new LinkedList<>();
        distances.put(starting, 0);
        queue.add(new State(starting, null, 0));
        while (!queue.isEmpty()) {
            State next = queue.poll();
            Point2 pos = next.pos();
            Direction dir = next.direction();
            int distance = next.distance();
            List<Direction> dirs;
            if (dir == null) {
                dirs = new ArrayList<>();
                for (Direction direction : Direction.values()) {
                    if (Set.of(tiles.get(direction.offset(pos)).directions).contains(direction.opposite())) {
                        dirs.add(direction);
                    }
                }
            } else {
                dirs = List.of(tiles.get(pos).getOpposite(dir.opposite()));
            }

            int newDistance = distance + 1;
            for (Direction direction : dirs) {
                Point2 check = direction.offset(pos);
                if (!distances.containsKey(check) || distances.getInt(check) > newDistance) {
                    distances.put(check, newDistance);
                    queue.add(new State(check, direction, newDistance));
                }
            }

        }

        for (Point2 pos : distances.keySet()) {
            tiles.put(pos, Type.LOOP);
        }

        Queue<Point2> toMark = new LinkedList<>();
        int maxX = tiles.keySet().stream().mapToInt(Point2::x).max().getAsInt();
        int maxY = tiles.keySet().stream().mapToInt(Point2::y).max().getAsInt();
        for (int y = 0; y <= maxY; y++) {
            toMark.add(new Point2(0, y));
            toMark.add(new Point2(maxX, y));
        }

        for (int x = 0; x <= maxX; x++) {
            toMark.add(new Point2(x, 0));
            toMark.add(new Point2(x, maxY));
        }

        while (!toMark.isEmpty()) {
            Point2 check = toMark.poll();
            if (tiles.containsKey(check) && tiles.get(check) != Type.LOOP && tiles.get(check) != Type.OUTSIDE) {
                tiles.put(check, Type.OUTSIDE);
                for (Direction dir : Direction.values()) {
                    Point2 newCheck = check.offset(dir);
                    toMark.add(newCheck);
                }
            }
        }
        return new Result(distances.values().intStream().max().getAsInt(), tiles.values().stream().filter(tile -> tile != Type.LOOP && tile != Type.OUTSIDE).count());
    }

    public static record State(Point2 pos, Direction direction, int distance) {}

}
