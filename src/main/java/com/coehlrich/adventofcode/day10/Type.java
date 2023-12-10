package com.coehlrich.adventofcode.day10;

import com.coehlrich.adventofcode.util.Direction;

public enum Type {
    VERTICAL(Direction.UP, Direction.DOWN),
    HORIZONTAL(Direction.LEFT, Direction.RIGHT),
    NORTH_EAST(Direction.UP, Direction.RIGHT),
    NORTH_WEST(Direction.UP, Direction.LEFT),
    SOUTH_WEST(Direction.DOWN, Direction.LEFT),
    SOUTH_EAST(Direction.DOWN, Direction.RIGHT),
    GROUND,
    STARTING;

    public final Direction[] directions;

    private Type(Direction... directions) {
        this.directions = directions;
    }

    public Direction getOpposite(Direction direction) {
        if (directions.length == 0) {
            return null;
        }
        if (directions[0] == direction) {
            return directions[1];
        } else {
            return directions[0];
        }
    }
}
