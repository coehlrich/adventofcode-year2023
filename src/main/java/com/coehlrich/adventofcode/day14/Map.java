package com.coehlrich.adventofcode.day14;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;

import java.util.Arrays;

public record Map(Type[][] map) {
    public int calculate(boolean part2) {
        Object2LongMap<Type[][]> cache = new Object2LongOpenCustomHashMap<>(new Hash.Strategy<Type[][]>() {

            @Override
            public int hashCode(Type[][] o) {
                return Arrays.deepHashCode(o);
            }

            @Override
            public boolean equals(Type[][] a, Type[][] b) {
                if (b != null) {
//                    for (int y = 0; y < a.length; y++) {
//                        for (int x = 0; x < a[y].length; x++) {
//                            System.out.print(switch (a[y][x]) {
//                                case ROUND -> 'O';
//                                case CUBE -> '#';
//                                case EMPTY -> '.';
//                            });
//                        }
//                        System.out.println();
//                    }
//                    System.out.println();
//                    for (int y = 0; y < b.length; y++) {
//                        for (int x = 0; x < b[y].length; x++) {
//                            System.out.print(switch (b[y][x]) {
//                                case ROUND -> 'O';
//                                case CUBE -> '#';
//                                case EMPTY -> '.';
//                            });
//                        }
//                        System.out.println();
//                    }
//                    System.out.println();
                }
                return Arrays.deepEquals(a, b);
            }

        });
        Type[][] map = this.map;
//        Int2ObjectMap<Type[][]> seen = new Int2ObjectOpenHashMap<>();
        for (long i = 0; i < (part2 ? 1_000_000_000 : 1); i++) {
            long cached = cache.getLong(map);
            if (cached > 0) {
                long step = i - cached;
//                System.out.println(i);
//                System.out.println(cached);
                while (i + step < 1_000_000_000) {
                    i += step;
                }
//                System.out.println(step);
            } else {
                cache.put(map, i);
            }

//            int hash = Arrays.deepHashCode(map);
//            if (seen.containsKey(hash)) {
//                for (int y = 0; y < map.length; y++) {
//                    for (int x = 0; x < map[y].length; x++) {
//                        System.out.print(switch (map[y][x]) {
//                            case EMPTY -> '.';
//                            case ROUND -> 'O';
//                            case CUBE -> '#';
//                        });
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//
//                Type[][] cachedMap = seen.get(hash);
//                for (int y = 0; y < cachedMap.length; y++) {
//                    for (int x = 0; x < cachedMap[y].length; x++) {
//                        System.out.print(switch (cachedMap[y][x]) {
//                            case EMPTY -> '.';
//                            case ROUND -> 'O';
//                            case CUBE -> '#';
//                        });
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            } else if (i > 0) {
//                seen.put(hash, map);
//            }
            for (int rot = 0; rot < (part2 ? 4 : 1); rot++) {
                Int2IntMap yPositions = new Int2IntOpenHashMap();
                Type[][] newMap = new Type[map.length][map[0].length];
                for (int y = 0; y < map.length; y++) {
                    for (int x = 0; x < map[y].length; x++) {
                        newMap[y][x] = map[y][x];
                    }
                }
                map = newMap;
                for (int y = 0; y < map.length; y++) {
                    for (int x = 0; x < map[y].length; x++) {
                        Type type = map[y][x];
                        if (type == Type.ROUND) {
                            map[y][x] = Type.EMPTY;
                            map[yPositions.get(x)][x] = Type.ROUND;
                            yPositions.put(x, yPositions.get(x) + 1);
                        } else if (type == Type.CUBE) {
                            yPositions.put(x, y + 1);
                        }
                    }
                }

                if (part2) {
//                    for (int y = 0; y < map.length; y++) {
//                        for (int x = 0; x < map[y].length; x++) {
//                            System.out.print(switch (map[y][x]) {
//                                case EMPTY -> '.';
//                                case ROUND -> 'O';
//                                case CUBE -> '#';
//                            });
//                        }
//                        System.out.println();
//                    }
//                    System.out.println();
                    newMap = new Type[map[0].length][map.length];
                    for (int y = 0; y < map.length; y++) {
                        for (int x = 0; x < map[y].length; x++) {
                            newMap[x][newMap[y].length - 1 - y] = map[y][x];
                        }
                    }
                    map = newMap;
//                    for (int y = 0; y < map.length; y++) {
//                        for (int x = 0; x < map[y].length; x++) {
//                            System.out.print(switch (map[y][x]) {
//                                case EMPTY -> '.';
//                                case ROUND -> 'O';
//                                case CUBE -> '#';
//                            });
//                        }
//                        System.out.println();
//                    }
//                    System.out.println();
                }
            }
            if (i <= 100) {
//                for (int y = 0; y < map.length; y++) {
//                    for (int x = 0; x < map[y].length; x++) {
//                        System.out.print(switch (map[y][x]) {
//                            case EMPTY -> '.';
//                            case ROUND -> 'O';
//                            case CUBE -> '#';
//                        });
//                    }
//                    System.out.println();
//                }
//                System.out.println();
            }
        }

        int result = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                Type type = map[y][x];
                if (type == Type.ROUND) {
                    result += map.length - y;
                }
            }
        }
        return result;
    }
}
