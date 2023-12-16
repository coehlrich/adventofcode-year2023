package com.coehlrich.adventofcode.day16;

import com.coehlrich.adventofcode.util.Direction;

import java.util.List;

public record Mirror(boolean downRight) implements Tile {

    @Override
    public List<Direction> beam(Direction dir) {
        Direction result = switch (dir) {
            case UP -> Direction.RIGHT;
            case LEFT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case RIGHT -> Direction.UP;
        };
        if (downRight) {
            result = result.opposite();
        }
        return List.of(result);
    }

}
