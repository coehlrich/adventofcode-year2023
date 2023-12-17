package com.coehlrich.adventofcode.day17;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.PriorityQueue;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        int[][] map = input.lines()
                .map(line -> line.chars()
                        .map(character -> Integer.parseInt(Character.toString(character)))
                        .toArray())
                .toArray(int[][]::new);

        return new Result(calculate(map, 0, 3), calculate(map, 4, 10));
    }

    private int calculate(int[][] map, int minSteps, int maxSteps) {
        Point2 start = new Point2(0, 0);
        Point2 end = new Point2(map[0].length - 1, map.length - 1);
        PriorityQueue<State> states = new PriorityQueue<>((s1, s2) -> {
            if (s1.heatLoss() != s2.heatLoss()) {
                return s1.heatLoss() - s2.heatLoss();
            } else {
                return s1.pos().distance(end) - s2.pos().distance(end);
            }
        });
        Object2IntMap<Cache> min = new Object2IntOpenHashMap<>();
        states.add(new State(start, null, 0, 0));
        while (!states.isEmpty()) {
            State state = states.poll();
//            System.out.println(state.pos());
            for (Direction dir : Direction.values()) {
                if (state.dir() == null ||
                        dir != state.dir().opposite() &&
                                (state.steps() < maxSteps || dir != state.dir()) &&
                                (state.steps() >= minSteps || dir == state.dir())) {
                    Point2 newPos = dir.offset(state.pos());
                    if (!start.equals(newPos) && newPos.x() >= 0 && newPos.x() < map[0].length && newPos.y() >= 0 && newPos.y() < map.length) {
                        int heatLoss = state.heatLoss() + map[newPos.y()][newPos.x()];
                        if (end.equals(newPos)) {
//                            for (int y = 0; y < map.length; y++) {
//                                for (int x = 0; x < map[0].length; x++) {
//                                    int steps = state.history().getInt(new Point2(x, y));
//                                    System.out.print(state.history().containsKey(new Point2(x, y)) ? ("(" + (Integer.toString(steps).length() < 2 ? "0" : "") + state.history().getInt(new Point2(x, y)) + ")") : "( .)");
//                                }
//                                System.out.println();
//                            }
//                            System.out.println(state.history());
                            return heatLoss;
                        }
                        int steps = state.dir() == dir ? state.steps() + 1 : 1;
                        Cache cacheKey = new Cache(newPos, dir, steps);
                        if (!min.containsKey(cacheKey) || min.getInt(cacheKey) > heatLoss) {
//                        Set<Point2> history = new HashSet<>(state.history());
//                        if (history.add(newPos)) {
                            states.add(new State(newPos, dir, state.dir() == dir ? state.steps() + 1 : 1, heatLoss));
                            min.put(cacheKey, heatLoss);
//                        }
//                            min.put(newPos, heatLoss);
                        }
                    }
                }
            }
        }
        return -1;
    }

}
