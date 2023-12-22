package com.coehlrich.adventofcode.day22;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Point3;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Map<Brick, List<Point3>> bricksToTiles = input.lines()
                .map(Brick::parse)
                .collect(Collectors.toMap(Function.identity(), Brick::getTiles));

        Map<Point3, Brick> tilesToBricks = new HashMap<>(bricksToTiles.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(tile -> Map.entry(tile, entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        Set<Point3> toLower = new HashSet<>(tilesToBricks.keySet());
        int maxZ = toLower.stream().mapToInt(Point3::z).max().getAsInt();
        
        Map<Brick, Set<Brick>> supported = new HashMap<>();
        Map<Brick, Set<Brick>> supportedBy = new HashMap<>();
        for (int z = 1; z <= maxZ; z++) {
            int f_z = z;
            List<Brick> bricks = toLower.stream()
                    .filter(point -> point.z() == f_z)
                    .map(tilesToBricks::get)
                    .distinct()
                    .toList();

            for (Brick brick : bricks) {
                List<Point3> tiles = bricksToTiles.get(brick);
                List<Point3> newTiles = tiles;
                tiles.forEach(tilesToBricks::remove);
                tiles.forEach(toLower::remove);
                bricksToTiles.remove(brick);
                while (newTiles.stream().allMatch(tile -> tile.z() > 0 && !tilesToBricks.containsKey(tile))) {
                    tiles = newTiles;
                    newTiles = newTiles.stream().map(point -> new Point3(point.x(), point.y(), point.z() - 1)).toList();
                }
                bricksToTiles.put(brick, tiles);
                Set<Brick> others = new HashSet<>();
                for (Point3 tile : tiles) {
                    Brick otherBrick = tilesToBricks.get(new Point3(tile.x(), tile.y(), tile.z() - 1));
                    if (otherBrick != null && otherBrick != brick) {
                        supported.computeIfAbsent(otherBrick, b -> new HashSet<>()).add(brick);
                        others.add(otherBrick);
                    }
                    tilesToBricks.put(tile, brick);
                }
                supportedBy.put(brick, others);
            }
        }

        int part1 = 0;
        Set<Brick> toCount = new HashSet<>();
        for (Brick brick : bricksToTiles.keySet()) {
            Set<Brick> supportedBricks = supported.get(brick);
            if (supportedBricks == null) {
                supportedBricks = Set.of();
            }
//            System.out.println(brick + " => " + supportedBricks.stream().map(supportedBy::get).toList());
            if (supportedBricks.stream().map(supportedBy::get)
                    .allMatch(bricks -> bricks.size() != 1)) {
                part1++;
            } else {
                toCount.add(brick);
            }
        }

        int part2 = 0;
        for (Brick brick : toCount) {
            Set<Brick> broken = new HashSet<>();
            broken.add(brick);

            Queue<Brick> toBreak = new LinkedList<>();
            toBreak.add(brick);
            while (!toBreak.isEmpty()) {
                Brick breaking = toBreak.poll();
                Set<Brick> check = supported.get(breaking);
                if (check == null) {
                    check = Set.of();
                }
                for (Brick other : check.stream().filter(Predicate.not(broken::contains)).toList()) {
                    Set<Brick> otherSupporting = supportedBy.get(other);
                    if (otherSupporting.stream().allMatch(broken::contains)) {
                        broken.add(other);
                        toBreak.add(other);
                    }
                }
            }
            part2 += broken.size() - 1;
        }
        return new Result(part1, part2);
    }

}
