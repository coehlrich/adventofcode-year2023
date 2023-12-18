package com.coehlrich.adventofcode.day18;

public record Square(long minX, long maxX, long minY, long maxY) {
    public long area() {
        return ((maxX - minX) + 1) * ((maxY - minY) + 1);
    }
}
