package com.coehlrich.adventofcode.day17;

import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

public record State(Point2 pos, Direction dir, int steps, int heatLoss, Object2IntMap<Point2> history) {

}
