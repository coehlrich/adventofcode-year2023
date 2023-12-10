package com.coehlrich.adventofcode.day10;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        Queue<Point2> toMark = new LinkedList<>();
        int maxX = tiles.keySet().stream().mapToInt(Point2::x).max().getAsInt();
        int maxY = tiles.keySet().stream().mapToInt(Point2::y).max().getAsInt();

        Map<Point2, AreaType> areaTypes = new HashMap<>();
        for (int x = 0; x <= maxX * 3 + 2; x++) {
            for (int y = 0; y <= maxY * 3 + 2; y++) {
                areaTypes.put(new Point2(x, y), AreaType.INSIDE);
            }
        }

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

                for (Type type : Type.values()) {
                    if (Stream.of(type.directions).toList().containsAll(dirs)) {
                        tiles.put(pos, type);
                    }
                }
            } else {
                dirs = List.of(tiles.get(pos).getOpposite(dir.opposite()));
            }

            Point2 base = new Point2(pos.x() * 3 + 1, pos.y() * 3 + 1);
            areaTypes.put(base, AreaType.LOOP);
            for (Direction direction : dirs) {
                areaTypes.put(direction.offset(base), AreaType.LOOP);
            }

            if (dirs.size() == 1) {
                areaTypes.put(dir.opposite().offset(base), AreaType.LOOP);
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

//        for (int x = 0; x <= maxX; x++) {
//            for (int y = 0; y <= maxY; y++) {
//                Type type = tiles.get(new Point2(x, y));
//                Point2 base = new Point2(x * 3 + 1, y * 3 + 1);
//                if (type.directions.length > 0) {
//                    areaTypes.put(base, AreaType.LOOP);
//                    for (Direction direction : type.directions) {
//                        areaTypes.put(direction.offset(base), AreaType.LOOP);
//                    }
//                }
//            }
//        }
        toMark.add(new Point2(0, 0));
        toMark.add(new Point2(maxX * 3 + 2, 0));
        toMark.add(new Point2(0, maxY * 3 + 2));
        toMark.add(new Point2(maxX * 3 + 2, maxY * 3 + 2));
        for (int y = 1; y < maxY * 3 + 2; y++) {
            toMark.add(new Point2(0, y));
            toMark.add(new Point2(maxX * 3 + 2, y));
        }

        for (int x = 1; x < maxX * 3 + 2; x++) {
            toMark.add(new Point2(x, 0));
            toMark.add(new Point2(x, maxY * 3 + 2));
        }

        tiles = tiles.entrySet().stream()
                .map(entry -> {
                    entry.setValue(!distances.containsKey(entry.getKey()) ? Type.GROUND : entry.getValue());
                    return entry;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        while (!toMark.isEmpty()) {
            Point2 check = toMark.poll();
            if (areaTypes.get(check) == AreaType.INSIDE) {
                areaTypes.put(check, AreaType.OUTSIDE);
                for (Direction dir : Direction.values()) {
                    Point2 newCheck = dir.offset(check);
                    toMark.add(newCheck);
                }
            }
        }

//        CompoundTag tag = new CompoundTag();
//        tag.putInt("DataVersion", 3700);
//
//        ListTag<IntTag> size = new ListTag<>(IntTag.class);
//        size.addInt(1);
//        size.addInt(1);
//        size.addInt(1);
//        tag.put("size", size);
//
//        ListTag<CompoundTag> palette = new ListTag<>(CompoundTag.class);
//        CompoundTag air = new CompoundTag();
//        air.putString("Name", "minecraft:air");
//        palette.add(air);
//
//        CompoundTag start = new CompoundTag();
//        start.putString("Name", "minecraft:white_wool");
//        palette.add(start);
//
//        CompoundTag outside = new CompoundTag();
//        outside.putString("Name", "minecraft:stone");
//        palette.add(outside);
//
//        for (Direction dir : Direction.values()) {
//            CompoundTag block = new CompoundTag();
//            block.putString("Name", "minecraft:magenta_glazed_terracotta");
//            CompoundTag properties = new CompoundTag();
//            properties.putString("facing", switch (dir) {
//                case UP -> "south";
//                case DOWN -> "north";
//                case LEFT -> "east";
//                case RIGHT -> "west";
//            });
//
//            block.put("Properties", properties);
//            palette.add(block);
//        }
//        tag.put("palette", palette);
//
//        ListTag<CompoundTag> blocks = new ListTag<>(CompoundTag.class);
//        for (int y = 0; y <= maxY * 3 + 2; y++) {
//            for (int x = 0; x <= maxX * 3 + 2; x++) {
//                CompoundTag block = new CompoundTag();
//                ListTag<IntTag> pos = new ListTag<>(IntTag.class);
//                pos.addInt(x);
//                pos.addInt(0);
//                pos.addInt(y);
//                block.put("pos", pos);
//                block.putInt("state", switch (areaTypes.get(new Point2(x, y))) {
//                    case LOOP -> 1;
//                    case OUTSIDE -> 0;
//                    case INSIDE -> 2;
//                });
//                blocks.add(block);
//            }
//        }
//        tag.put("blocks", blocks);
//
////        Point2 pos = starting;
////        Direction dir = Direction.RIGHT;
////        do {
////            Type type = tiles.get(pos);
////            if (type.directions.length > 0) {
////                dir = type.getOpposite(dir.opposite());
////            }
////            {
////                CompoundTag block = new CompoundTag();
////                ListTag<IntTag> posTag = new ListTag<>(IntTag.class);
////                posTag.addInt(pos.x());
////                posTag.addInt(0);
////                posTag.addInt(pos.y());
////                block.put("pos", posTag);
////                block.putInt("state", 2 + dir.ordinal());
////                blocks.add(block);
////            }
////            if (pos.equals(starting)) {
////                CompoundTag block = new CompoundTag();
////                ListTag<IntTag> posTag = new ListTag<>(IntTag.class);
////                posTag.addInt(pos.x());
////                posTag.addInt(1);
////                posTag.addInt(pos.y());
////                block.put("pos", posTag);
////                block.putInt("state", 1);
////                blocks.add(block);
////            }
////            pos = dir.offset(pos);
////        } while (!pos.equals(starting));
//
//        NamedTag root = new NamedTag("root", tag);
//        try {
//            new File("2023-day10.nbt").createNewFile();
//            NBTUtil.write(root, new File("2023-day10.nbt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (int y = 0; y <= maxY * 3 + 2; y++) {
//            for (int x = 0; x <= maxX * 3 + 2; x++) {
//                System.out.print(switch (areaTypes.get(new Point2(x, y))) {
//                    case INSIDE -> '.';
//                    case OUTSIDE -> 'V';
//                    case LOOP -> 'X';
//                });
//            }
//            System.out.println();
//        }

        int part2 = 0;
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                List<AreaType> areaTypesInArea = new ArrayList<>();
                for (int dy = 0; dy < 3; dy++) {
                    for (int dx = 0; dx < 3; dx++) {
                        areaTypesInArea.add(areaTypes.get(new Point2(x * 3 + dx, y * 3 + dy)));
                    }
                }
                if (!areaTypesInArea.contains(AreaType.LOOP) && !areaTypesInArea.contains(AreaType.OUTSIDE)) {
                    part2++;
                }
//                
//                if (areaTypesInArea.contains(AreaType.LOOP)) {
//                    System.out.print('X');
//                } else if (areaTypesInArea.contains(AreaType.OUTSIDE)) {
//                    System.out.println('I');
//                } else {
//                    System.out.println
//                }
            }
        }
        return new Result(distances.values().intStream().max().getAsInt(), part2);
    }

    public static record State(Point2 pos, Direction direction, int distance) {}

}
