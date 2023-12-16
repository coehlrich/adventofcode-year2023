package com.coehlrich.adventofcode.day16;

import com.coehlrich.adventofcode.util.Direction;

import java.util.List;

public record Splitter(boolean horizontal) implements Tile {

    @Override
    public List<Direction> beam(Direction dir) {
        if (horizontal && Direction.HORIZONTAL.contains(dir) || !horizontal && Direction.VERTICAL.contains(dir)) {
            return List.of(dir);
        } else {
            return List.copyOf(horizontal ? Direction.HORIZONTAL : Direction.VERTICAL);
        }
    }

}
