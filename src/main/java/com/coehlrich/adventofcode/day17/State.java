package com.coehlrich.adventofcode.day17;

import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;

public record State(Point2 pos, Direction dir, int steps, int heatLoss) {

}
