package com.coehlrich.adventofcode.day23;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.*;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Tile[][] map = input.lines()
                .map(line -> line.chars()
                        .mapToObj(character -> switch (character) {
                            case '#' -> new Tile(Type.SOLID, null);
                            case '.' -> new Tile(Type.EMPTY, null);
                            case 'v' -> new Tile(Type.STEEP, Direction.DOWN);
                            case '>' -> new Tile(Type.STEEP, Direction.RIGHT);
                            default -> throw new IllegalArgumentException("Unexpected value: " + character);
                        })
                        .toArray(Tile[]::new))
                .toArray(Tile[][]::new);

        Point2 start = new Point2(1, 0);
        Map<Point2, List<State>> cache = new HashMap<>();
        Object2IntMap<Part2State> states = new Object2IntOpenHashMap<>(Map.of(new Part2State(start, Set.of(start)), 0));

        Point2 end = new Point2(map[0].length - 2, map.length - 1);
        int part2 = 0;
        while (!states.isEmpty()) {
            System.out.println(states.size());
            Object2IntMap<Part2State> newStates = new Object2IntOpenHashMap<>();
            for (Object2IntMap.Entry<Part2State> state : states.object2IntEntrySet()) {
                Point2 pos = state.getKey().pos();
                List<State> visiting = cache.computeIfAbsent(pos, key -> calculate(map, pos, true));
                for (State s : visiting) {
                    Set<Point2> visited = new HashSet<>(state.getKey().history());
                    if (end.equals(s.pos())) {
                        part2 = Math.max(state.getIntValue() + s.steps(), part2);
                    } else if (visited.add(s.pos())) {
                        int steps = state.getIntValue() + s.steps();
                        Part2State newS = new Part2State(s.pos(), visited);
                        newStates.put(newS, Math.max(steps, newStates.getInt(newS)));
                    }
                }
            }
            states = newStates;
        }

        return new Result(calculate(map, new Point2(1, 0), false).get(0).steps(), part2);
    }

    private List<State> calculate(Tile[][] map, Point2 start, boolean part2) {
        Queue<State> states = new LinkedList<>();
        states.add(new State(start, null, 0));
        Point2 end = new Point2(map[0].length - 2, map.length - 1);
        Object2IntMap<Point2> merged = new Object2IntOpenHashMap<>();
        List<State> endList = new ArrayList<>();
        while (!states.isEmpty()) {
//            System.out.println(states.size());
            State state = states.poll();
            for (Direction dir : Direction.values()) {
                int steps = state.steps() + 1;
                Point2 pos = dir.offset(state.pos());
                Point2 previous = state.previous();
                if (pos.y() >= 0 && !pos.equals(previous) && map[pos.y()][pos.x()].type() != Type.SOLID && (part2 || map[pos.y()][pos.x()].dir() != dir.opposite())) {
                    Tile tile = map[pos.y()][pos.x()];
                    previous = state.pos();
                    if (!part2 && tile.type() == Type.STEEP) {
                        previous = pos;
                        pos = tile.dir().offset(pos);
                        steps++;

                        if (map[pos.y() - 1][pos.x()].dir() == Direction.DOWN && map[pos.y()][pos.x() - 1].dir() == Direction.RIGHT) {
                            if (!merged.containsKey(pos)) {
                                merged.put(pos, steps);
                                break;
                            } else {
                                steps = Math.max(merged.getInt(pos), steps);
                            }
                        }
                    }

                    State endState = new State(pos, previous, steps);

                    if (end.equals(pos) && part2) {
                        return List.of(endState);
                    } else if (end.equals(pos)) {
                        endList.add(endState);
                    } else if (part2 && pos.y() > 0 &&
                            map[pos.y() - 1][pos.x()].type() != Type.EMPTY &&
                            map[pos.y() + 1][pos.x()].type() != Type.EMPTY &&
                            map[pos.y()][pos.x() - 1].type() != Type.EMPTY &&
                            map[pos.y()][pos.x() + 1].type() != Type.EMPTY) {
                            endList.add(endState);
                            continue;
                    } else {
                        states.add(endState);
                    }
                }
            }
        }
        return endList.stream().sorted((s1, s2) -> s2.steps() - s1.steps()).toList();
    }

}
