package com.coehlrich.adventofcode.day19;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Part(int x, int m, int a, int s) {

    private static final Pattern PATTERN = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}");

    public static Part parse(String line) {
        Matcher matcher = PATTERN.matcher(line);
        matcher.matches();
        return new Part(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
    }
}
