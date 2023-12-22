package com.coehlrich.adventofcode.day21;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Type[][] map = input.lines()
                .map(line -> line.chars().mapToObj(character -> switch (character) {
                    case '.' -> Type.EMPTY;
                    case '#' -> Type.ROCK;
                    case 'S' -> Type.START;
                    default -> throw new IllegalArgumentException("Unexpected value: " + character);
                })
                        .toArray(Type[]::new))
                .toArray(Type[][]::new);

        Point2 start = null;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == Type.START) {
                    start = new Point2(x, y);
                }
            }
        }

//        Set<Point2> visited = new HashSet<>();
//        Set<Point2> visiting = new HashSet<>();
//        visited.add(start);
//        visiting.add(start);
//        int part1 = 1;
//        for (int i = 0; i < 6; i++) {
//            Set<Point2> newVisiting = new HashSet<>();
//            for (Point2 visit : visiting) {
//                for (Direction dir : Direction.values()) {
//                    Point2 newTile = dir.offset(visit);
//                    if (newTile.y() >= 0 && newTile.y() < map.length &&
//                            newTile.x() >= 0 && newTile.x() < map[0].length &&
//                            map[newTile.y()][newTile.x()] != Type.ROCK) {
//                        if (visited.add(newTile)) {
//                            newVisiting.add(newTile);
//                            if (i % 2 == 1) {
//                                part1++;
//                            }
//                        }
//                    }
//                }
//            }
//            visiting = newVisiting;
//        }

//        long steps = 6;
//        long count = 0;
//        for (int y = 0; y < map.length; y++) {
//            for (int x = 0; x < map[y].length; x++) {
//                if (visited.contains(new Point2(x, y))) {
//                    System.out.print('O');
//                } else {
//                    System.out.print(switch (map[y][x]) {
//                        case EMPTY -> '.';
//                        case ROCK -> '#';
//                        case START -> 'S';
//                    });
//                }
//                if (map[y][x] != Type.ROCK && y % 2 == 0 && x % 2 == 0) {
//                    count++;
//                }
//            }
//            System.out.println();
//        }
//        System.out.println(map.length);

//        Map<Point2, Object2IntMap<Point2>> distances = new HashMap<>();
//        Set<Point2> edges = new HashSet<>();
//        distances.put(start, visit(map, start));
//        Point2 pos = new Point2(0, 0);
//        distances.put(pos, visit(map, pos));
//        pos = new Point2(0, map.length - 1);
//        distances.put(pos, visit(map, pos));
//        pos = new Point2(map.length - 1, 0);
//        distances.put(pos, visit(map, pos));
//        pos = new Point2(map.length - 1, map.length - 1);
//        distances.put(pos, visit(map, pos));
//
//        pos = new Point2(map.length / 2, 0);
//        distances.put(pos, visit(map, pos));
//        pos = new Point2(0, map.length / 2);
//        distances.put(pos, visit(map, pos));
//        pos = new Point2(map.length - 1, map.length / 2);
//        distances.put(pos, visit(map, pos));
//        pos = new Point2(map.length / 2, map.length - 1);
//        distances.put(pos, visit(map, pos));

//        for (Point2 key : distances.keySet()) {
//            System.out.println(key + ": " + distances.get(key).values().intStream().max().getAsInt());
//            System.out.println(key + ": " + distances.get(key).values().intStream().filter(num -> num % 2 == 0 && num <= 10).count());
//        }
//        for (int i = 0; i <= 20; i++) {
//            int f_i = i;
//            System.out.println(distances.get(new Point2(map.length - 1, map.length - 1)).values().intStream().filter(num -> num % 2 == 0 && num <= f_i).count());
//        }
//        for (int y = 0; y < map.length; y++) {
//            Point2 pos = new Point2(0, y);
//            distances.put(pos, visit(map, pos));
//            edges.add(pos);
//            pos = new Point2(map[y].length - 1, y);
//            distances.put(pos, visit(map, pos));
//            edges.add(pos);
//        }
//        for (int x = 0; x < map.length; x++) {
//            Point2 pos = new Point2(x, 0);
//            distances.put(pos, visit(map, pos));
//            edges.add(pos);
//            pos = new Point2(x, map.length - 1);
//            distances.put(pos, visit(map, pos));
//            edges.add(pos);
//        }

        long steps = 26501365;
//        for (Point2 key : distances.keySet()) {
//            System.out.println(key + ": " + distances.get(key).values().intStream().max().getAsInt());
//            int value = distances.get(key).values().intStream().filter(num -> num % 2 == steps % 2).max().getAsInt();
//            if (value >= 260) {
//            Object2IntMap<Point2> distance = distances.get(key);
//            System.out.println(key + ": " + distance.keySet().stream().filter(edges::contains).mapToInt(distance::getInt).min().getAsInt());
//            System.out.println(key + ": " + distances.get(key).values().intStream().filter(num -> num % 2 == steps % 2 && num <= 65).count());
//            }
//        }
//        System.out.println(26501365 / map.length);
//        System.out.println(26501365 % map.length);
//        System.out.println(map.length);
        long loops = steps / map.length;
        long remaining = steps % map.length;

        Set<Point2> visited = new HashSet<>();
        Set<Point2> visiting = new HashSet<>();
        visited.add(start);
        visiting.add(start);
        Set<Point2> newVisiting = new HashSet<>();
        long result = steps % 2 == 0 ? 1 : 0;
        int part1 = 1;
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
        List<WeightedObservedPoint> points = new ArrayList<>();
        for (int i = 0; i <= 4 * map.length + remaining; i++) {
//            if (i == 6 ||
//                    i == 10 ||
//                    i == 50 ||
//                    i == 100 ||
//                    i == 500 ||
//                    i == 1000 ||
//                    i == 5000) {
//                System.out.println(result);
//            }
            if (i % (map.length * 2) == remaining) {
//                System.out.println(result);
//                System.out.println(i);
                points.add(new WeightedObservedPoint(1, i / map.length / 2, result));
//                System.out.println(i / map.length / 2);
            }
            newVisiting = new HashSet<>();
            for (Point2 visit : visiting) {
                for (Direction dir : Direction.values()) {
                    Point2 newTile = dir.offset(visit);
                    if (map[mod(newTile.y(), map.length)][mod(newTile.x(), map.length)] != Type.ROCK) {
                        if (visited.add(newTile)) {
                            newVisiting.add(newTile);
                            if (i % 2 != steps % 2) {
                                result++;
                            }

                            if (i < 64 && i % 2 == 1) {
                                part1++;
                            }
                        }
                    }
                }
            }
            visiting = newVisiting;
        }

        double[] problem = fitter.fit(points);
//        System.out.println(Arrays.toString(problem));
//        System.out.println("x=" + (65 + 131 * 0) + " y=" + visit(map, start, true, 65 + 131 * 0));
//        System.out.println("x=" + (65 + 131 * 1) + " y=" + visit(map, start, true, 65 + 131 * 1));
//        System.out.println("x=" + (65 + 131 * 2) + " y=" + visit(map, start, true, 65 + 131 * 2));
//        System.out.println("x=" + (65 + 131 * 3) + " y=" + visit(map, start, true, 65 + 131 * 3));
//        System.out.println("x=" + (65 + 131 * 4) + " y=" + visit(map, start, true, 65 + 131 * 4));
//        System.out.println("x=" + (65 + 131 * 5) + " y=" + visit(map, start, true, 65 + 131 * 5));
//        System.out.println(distances.get(start).values().intStream().filter(num -> num % 2 == steps % 2).max().getAsInt());
        long x = loops / 2;
        System.out.println("y = " + Math.round(problem[2]) + "x^2 + " + Math.round(problem[1]) + "x + " + Math.round(problem[0]));
        return new Result(
                part1,
                (long) Math.round((problem[2] * x * x) + (problem[1] * x) + problem[0]));
    }

    private Object2IntMap<Point2> visit(Type[][] map, Point2 start) {
        Set<Point2> visited = new HashSet<>();
        Set<Point2> visiting = new HashSet<>();
        visited.add(start);
        visiting.add(start);
        Set<Point2> newVisiting = new HashSet<>();
        int i = 0;
        Object2IntOpenHashMap<Point2> result = new Object2IntOpenHashMap<>();
        result.put(start, 0);
        do {
            i++;
            newVisiting = new HashSet<>();
            for (Point2 visit : visiting) {
                for (Direction dir : Direction.values()) {
                    Point2 newTile = dir.offset(visit);
                    if (newTile.y() >= 0 && newTile.y() < map.length &&
                            newTile.x() >= 0 && newTile.x() < map[0].length &&
                            map[newTile.y()][newTile.x()] != Type.ROCK) {
                        if (visited.add(newTile)) {
                            newVisiting.add(newTile);
                            result.put(newTile, i);
                        }
                    }
                }
            }
            visiting = newVisiting;
        } while (!newVisiting.isEmpty());
        return result;
    }

    private long visit(Type[][] map, Point2 start, boolean part2, int count) {
        Set<Point2> visited = new HashSet<>();
        Set<Point2> visiting = new HashSet<>();
        visited.add(start);
        visiting.add(start);
        Set<Point2> newVisiting = new HashSet<>();
        long result = 1;
        for (int i = 0; i < count; i++) {
            newVisiting = new HashSet<>();
            for (Point2 visit : visiting) {
                for (Direction dir : Direction.values()) {
                    Point2 newTile = dir.offset(visit);
                    if ((part2 || newTile.y() >= 0 && newTile.y() < map.length &&
                            newTile.x() >= 0 && newTile.x() < map[0].length) &&
                            map[mod(newTile.y(), map.length)][mod(newTile.x(), map.length)] != Type.ROCK) {
                        if (visited.add(newTile)) {
                            newVisiting.add(newTile);
                            if (i % 2 != count % 2) {
                                result++;
                            }
                        }
                    }
                }
            }
            visiting = newVisiting;
        }
        return result;
    }

    private int mod(int value, int divisor) {
        return ((value % divisor) + divisor) % divisor;
    }

}
