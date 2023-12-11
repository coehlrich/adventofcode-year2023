package com.coehlrich.adventofcode.util;

public record Point2(int x, int y) {
    public int distance(Point2 o) {
        return Math.abs(o.x - x) + Math.abs(o.y - y);
    }
}
