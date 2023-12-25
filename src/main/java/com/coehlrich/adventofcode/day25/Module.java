package com.coehlrich.adventofcode.day25;

import java.util.List;
import java.util.stream.Stream;

public record Module(String name, List<String> connected) {

    public static Module parse(String line) {
        String[] parts = line.split(": ");
        List<String> connected = Stream.of(parts[1].split(" ")).toList();
        return new Module(parts[0], connected);
    }

}
