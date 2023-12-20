package com.coehlrich.adventofcode.day20;

import java.util.List;

public record Module(Type type, String name, List<String> outputs) {
    public static Module parse(String line) {
        String[] parts = line.split(" -> ");
        if (parts[0].equals("broadcaster")) {
            return new Module(Type.BROADCASTER, "broadcaster", List.of(parts[1].split(", ")));
        } else {
            return new Module(switch (parts[0].charAt(0)) {
                case '%' -> Type.FLIP_FLOP;
                case '&' -> Type.CONJUNCTION;
                default -> throw new IllegalArgumentException("Unexpected value: " + parts[0].charAt(0));
            }, parts[0].substring(1), List.of(parts[1].split(", ")));
        }
    }
}
