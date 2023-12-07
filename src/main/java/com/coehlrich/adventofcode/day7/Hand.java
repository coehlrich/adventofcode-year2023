package com.coehlrich.adventofcode.day7;

import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import it.unimi.dsi.fastutil.chars.CharSet;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public record Hand(String characters, int bid) {

    public static Hand parse(String text) {
        String[] split = text.split(" ");
        return new Hand(split[0], Integer.parseInt(split[1]));
    }

    public Type getType(boolean part2) {
        Int2IntMap count = new Int2IntOpenHashMap();
        CharSet set = new CharOpenHashSet();
        int jokers = 0;
        for (char character : characters.toCharArray()) {
            if (character == 'J') {
                jokers++;
            } else if (set.add(character)) {
                int amount = (int) characters.chars().filter(check -> check == character).count();
                count.put(amount, count.get(amount) + 1);
            }
        }

        if (jokers > 0) {
            int max = count.keySet().intStream().sorted().max().orElse(0);
            int newMax = max + jokers;
            if (max != 0) {
                count.put(max, count.get(max) - 1);
            }
            count.put(newMax, count.get(newMax) + 1);
        }
        for (Type type : Type.values()) {
            if (type.predicate.test(count)) {
                return type;
            }
        }
        throw new IllegalStateException(characters);
    }
}
