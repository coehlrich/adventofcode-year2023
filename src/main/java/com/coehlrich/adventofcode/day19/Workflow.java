package com.coehlrich.adventofcode.day19;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public record Workflow(String name, List<Rule> rules) {

    private static final Pattern PATTERN = Pattern.compile("(\\w+)\\{(.*)\\}");

    public static Workflow parse(String line) {
        Matcher matcher = PATTERN.matcher(line);
        matcher.matches();
        String name = matcher.group(1);
        List<Rule> rules = Stream.of(matcher.group(2).split(","))
                .map(Rule::parse)
                .toList();
        return new Workflow(name, rules);
    }

}
