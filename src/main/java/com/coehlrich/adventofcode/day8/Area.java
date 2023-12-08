package com.coehlrich.adventofcode.day8;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Area(String name, String left, String right) {

    private static final Pattern PATTERN = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");

    public static Area parse(String line) {
        Matcher matcher = PATTERN.matcher(line);
        matcher.matches();
        return new Area(matcher.group(1), matcher.group(2), matcher.group(3));
    }
}
