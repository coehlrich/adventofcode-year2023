package com.coehlrich.adventofcode.day13;

public record Map(int[][] map) {
    public static Map parse(String text) {
        return new Map(text.lines()
                .map(line -> line.chars()
                        .map(character -> switch (character) {
                            case '.' -> 0;
                            case '#' -> 1;
                            default -> throw new IllegalArgumentException("Unexpected value: " + character);
                        })
                        .toArray())
                .toArray(int[][]::new));
    }

    public int calculate(boolean part2) {
        int result = check(map, part2);
        if (result == -1) {
            int[][] rotated = new int[map[0].length][map.length];
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    rotated[x][y] = map[y][x];
                }
            }
            result = check(rotated, part2);
            if (result == -1) {
                throw new IllegalStateException();
            }
            return result;
        }
        return result * 100;
    }

    private int check(int[][] map, boolean part2) {
        for (int y = 1; y < map.length; y += 2) {
            if (checkRow(map[0], map[y], !part2).success()) {
                int result = checkMatch(map, 0, y, part2);
                if (result != -1) {
                    return result;
                }
            }
        }

        for (int y = map.length - 2; y >= 0; y -= 2) {
            if (checkRow(map[map.length - 1], map[y], !part2).success()) {
                int result = checkMatch(map, y, map.length - 1, part2);
                if (result != -1) {
                    return result;
                }
            }
        }

        return -1;

    }

    private int checkMatch(int[][] map, int start, int end, boolean part2) {
        int middle = (end - start - 1) / 2 + start;
        boolean smudged = !part2;
        for (int y = start; y <= middle; y++) {
            State state = checkRow(map[y], map[end - (y - start)], smudged);
            if (!state.success()) {
                return -1;
            }
            smudged = state.smudged();
        }
        if (!smudged) {
            return -1;
        }
        return middle + 1;
    }

    private State checkRow(int[] first, int[] second, boolean smudged) {
        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) {
                if (smudged) {
                    return new State(false, smudged);
                } else {
                    smudged = true;
                }
            }
        }
        return new State(true, smudged);
    }
}
