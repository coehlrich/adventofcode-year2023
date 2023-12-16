package com.coehlrich.adventofcode.day16;

import com.coehlrich.adventofcode.util.Direction;

import java.util.List;

public interface Tile {
    public List<Direction> beam(Direction dir);
}
