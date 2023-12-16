package com.coehlrich.adventofcode.day16;

import com.coehlrich.adventofcode.util.Direction;

import java.util.List;

public class Empty implements Tile {

    @Override
    public List<Direction> beam(Direction dir) {
        return List.of(dir);
    }

}
