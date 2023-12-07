package com.coehlrich.adventofcode.day7;

import it.unimi.dsi.fastutil.ints.Int2IntMap;

import java.util.function.Predicate;

public enum Type {
    FIVE_OF_A_KIND(map -> map.get(5) == 1),
    FOUR_OF_A_KIND(map -> map.get(4) == 1 && map.get(1) == 1),
    FULL_HOUSE(map -> map.get(3) == 1 && map.get(2) == 1),
    THREE_OF_A_KIND(map -> map.get(3) == 1 && map.get(1) == 2),
    TWO_PAIR(map -> map.get(2) == 2 && map.get(1) == 1),
    ONE_PAIR(map -> map.get(2) == 1 && map.get(1) == 3),
    HIGH_CARD(map -> map.get(1) == 5);

    public final Predicate<Int2IntMap> predicate;

    private Type(Predicate<Int2IntMap> predicate) {
        this.predicate = predicate;
    }
}
