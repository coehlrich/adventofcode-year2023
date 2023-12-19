package com.coehlrich.adventofcode.day19;

import java.util.function.IntPredicate;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Rule(String source, String type, int value, ToIntFunction<Part> getter, IntPredicate condition, String result) {

    private static final Pattern PATTERN = Pattern.compile("(\\w+)([<>])(\\d+):(\\w+)");

    public static Rule parse(String rule) {
        if (!rule.contains(":")) {
            return new Rule(null, null, -1, part -> 0, num -> true, rule);
        } else {
            Matcher matcher = PATTERN.matcher(rule);
            matcher.matches();
            String source = matcher.group(1);
            String type = matcher.group(2);
            int comparison = Integer.parseInt(matcher.group(3));
            String result = matcher.group(4);

            ToIntFunction<Part> getter = switch (source.charAt(0)) {
                case 'x' -> Part::x;
                case 'm' -> Part::m;
                case 'a' -> Part::a;
                case 's' -> Part::s;
                default -> throw new IllegalArgumentException("Unexpected value: " + source.charAt(0));
            };

            IntPredicate condition = switch (type.charAt(0)) {
                case '<' -> num -> num < comparison;
                case '>' -> num -> num > comparison;
                default -> throw new IllegalArgumentException("Unexpected value: " + type.charAt(0));
            };

            return new Rule(source, type, comparison, getter, condition, result);
        }
    }
}
