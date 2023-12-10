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
                        System.out.println(direction);
                        dirs.add(direction);
                    }
                }
            } else {
                System.out.println(tiles.get(pos));
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
        System.out.println(distances);
        return new Result(distances.values().intStream().max().getAsInt(), 0);
    }

    public static record State(Point2 pos, Direction direction, int distance) {}

}
