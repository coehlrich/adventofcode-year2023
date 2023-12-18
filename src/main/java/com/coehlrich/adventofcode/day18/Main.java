package com.coehlrich.adventofcode.day18;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import java.util.List;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<Instruction> instructions = input.lines().map(Instruction::parse).toList();

        return new Result(
                calculate(instructions.stream().map(Instruction::part1).toList()),
                calculate(instructions.stream().map(Instruction::part2).toList()));
    }

    private long calculate(List<Dig> steps) {
        long result = 0;
        long x = 1;
        long y = 1;
        for (int i = 0; i < steps.size(); i++) {
            Dig dig = steps.get(i);
            long dx = switch (dig.dir()) {
                case UP, DOWN -> 0;
                case LEFT -> -dig.steps();
                case RIGHT -> dig.steps();
            };
            long dy = switch (dig.dir()) {
                case LEFT, RIGHT -> 0;
                case UP -> -dig.steps();
                case DOWN -> dig.steps();
            };

            result += (x * (y + dy)) - ((x + dx) * y);

            x += dx;
            y += dy;
        }

        return result / 2 + steps.stream().mapToLong(Dig::steps).sum() / 2 + 1;

//        int minY = 0;
//        int maxY = 0;
//        int minX = 0;
//        int maxX = 0;
//        int x = 0;
//        int y = 0;
//        for (Dig dig : steps) {
//            switch (dig.dir()) {
//                case DOWN -> y += dig.steps();
//                case UP -> y -= dig.steps();
//                case LEFT -> x -= dig.steps();
//                case RIGHT -> x += dig.steps();
//            }
//            minY = Math.min(y, minY);
//            maxY = Math.max(y, maxY);
//            minX = Math.min(x, minX);
//            maxX = Math.max(x, maxX);
//        }
//
//        long lengthX = (maxX - minX) + 1;
//        long lengthY = (maxY - minY) + 1;
//        System.out.println(lengthX * lengthY);
//        boolean[][] map = new boolean[(maxY - minY) + 1][];
//        Point2 pos = new Point2(0, 0);
//        System.out.println((long) (map.length - 1) * (long) (map[0].length - 1));
//        return maxX;

//        List<Square> squares = new ArrayList<>();
//        long x = 0;
//        long y = 0;
//        for (Dig dig : steps) {
//            switch (dig.dir()) {
//                case DOWN -> {
//                    
//                }
//                case LEFT -> {
//                    
//                }
//                case RIGHT -> {
//                    
//                }
//                case UP -> {
//                    
//                }
//            }
//        }

//        long x = 0;
//        long result = 0;
//        Stack<Edge> edges = new Stack<>();
//        boolean exiting = false;
//        Dig previous = null;
//        for (Dig dir : steps) {
//            System.out.println(dir);
//            switch (dir.dir()) {
//                case DOWN, UP -> {
////                    if (!edges.isEmpty() && edges.peek().dir == null) {
////                        Edge edge = edges.pop();
////                    }
//                    if (!edges.isEmpty() && edges.peek().dir == dir.dir().opposite()) {
//                        if (!exiting) {
//                            edges.peek().yDistance -= 1;
//                            if (edges.peek().yDistance == 0) {
//                                edges.pop();
//                            }
//                        }
//                        exiting = true;
//                        if (previous.dir() == Direction.LEFT && x >= edges.peek().rightX) {
//                            System.out.println("Removing: " + previous.steps());
//                            result -= previous.steps();
//                        } else if (previous.dir() == Direction.RIGHT && x <= edges.peek().leftX) {
//                            System.out.println("Removing: " + previous.steps());
//                            result -= previous.steps();
//                        }
//                        int distance = dir.steps();
//                        Edge edge;
//                        do {
//                            edge = edges.peek();
//                            long traveled = Math.min(edge.yDistance, distance);
//                            long useX = x <= edge.leftX ? edge.leftX - x - 1 : x - edge.rightX - 1;
//                            if (edge.leftX != x && edge.rightX != x) {
//                                System.out.println(traveled * useX);
//                                result += traveled * useX;
//                                distance -= traveled;
//                            }
//                            edge.yDistance -= traveled;
//                            if (edge.yDistance == 0) {
//                                edges.pop();
//                            }
//                        } while (distance > 0 && !edges.isEmpty());
//                    } else {
//                        exiting = false;
//                        if (!edges.isEmpty() && previous != null && Direction.HORIZONTAL.contains(previous.dir())) {
//                            edges.peek().yDistance -= 1;
//                            Edge edge = new Edge();
//                            edge.dir = dir.dir();
//                            edge.leftX = previous.dir() == Direction.LEFT ? x : x - previous.steps();
//                            edge.rightX = previous.dir() == Direction.LEFT ? x + previous.steps() : x;
//                            edge.yDistance = 1;
//                            edges.push(edge);
//                        }
//                        Edge edge = new Edge();
//                        edge.dir = dir.dir();
//                        edge.leftX = x;
//                        edge.rightX = x;
//                        edge.yDistance = dir.steps();
//                        edges.push(edge);
//                    }
//                }
//                case LEFT -> {
//                    x -= dir.steps();
//                }
//                case RIGHT -> {
//                    x += dir.steps();
//                }
//            }
//            result += dir.steps();
//            previous = dir;
//
//        }
//        return result;
//        return bottomY;

//        Object2BooleanMap<Point2> map = new Object2BooleanOpenHashMap<>();
//        Point2 pos = new Point2(0, 0);
//        for (Dig instruction : steps) {
//            Direction dir = instruction.dir();
//            for (int i = 0; i < instruction.steps(); i++) {
//                pos = dir.offset(pos);
//                map.put(pos, true);
//            }
//        }
//
//        Point2 startDig = new Point2(1, 1);
//        Queue<Point2> toDig = new LinkedList<>();
//        toDig.add(startDig);
//        map.put(startDig, true);
//        while (!toDig.isEmpty()) {
//            Point2 dig = toDig.poll();
//            for (Direction dir : Direction.values()) {
//                Point2 newDig = dir.offset(dig);
//                if (!map.getBoolean(newDig)) {
//                    map.put(newDig, true);
//                    toDig.add(newDig);
//                }
//            }
//        }
//
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
//        start.putString("Name", "minecraft:grass_block");
//        palette.add(start);
//
//        CompoundTag origin = new CompoundTag();
//        origin.putString("Name", "minecraft:stone");
//        palette.add(origin);
//        tag.put("palette", palette);
//
//        int maxY = map.keySet().stream().mapToInt(Point2::y).max().getAsInt();
//        int minY = map.keySet().stream().mapToInt(Point2::y).min().getAsInt();
//        int maxX = map.keySet().stream().mapToInt(Point2::x).max().getAsInt();
//        int minX = map.keySet().stream().mapToInt(Point2::x).min().getAsInt();
//        ListTag<CompoundTag> blocks = new ListTag<>(CompoundTag.class);
//        for (int dy = minY; dy <= maxY; dy++) {
//            for (int dx = minX; dx <= maxX; dx++) {
//                CompoundTag block = new CompoundTag();
//                ListTag<IntTag> posTag = new ListTag<>(IntTag.class);
//                posTag.addInt(dx - minX);
//                posTag.addInt(0);
//                posTag.addInt(dy - minY);
//                block.put("pos", posTag);
//                block.putInt("state", dx == 0 && dy == 0 ? 2 : map.getBoolean(new Point2(dx, dy)) ? 0 : 1);
//                blocks.add(block);
//            }
//        }
//        tag.put("blocks", blocks);
//
//        NamedTag root = new NamedTag("root", tag);
//        try {
//            new File("2023-day18.nbt").createNewFile();
//            NBTUtil.write(root, new File("2023-day18.nbt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return map.values().stream().filter(bool -> bool).count();
    }

}
