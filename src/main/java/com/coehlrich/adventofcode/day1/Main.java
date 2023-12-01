package com.coehlrich.adventofcode.day1;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main implements Day {

    @Override
    public Result execute(String input) {

        Map<String, String> nameToNumber = new HashMap<>();
        nameToNumber.put("one", "1");
        nameToNumber.put("two", "2");
        nameToNumber.put("three", "3");
        nameToNumber.put("four", "4");
        nameToNumber.put("five", "5");
        nameToNumber.put("six", "6");
        nameToNumber.put("seven", "7");
        nameToNumber.put("eight", "8");
        nameToNumber.put("nine", "9");

        Map<String, String> joined = new HashMap<>();

        for (String key1 : nameToNumber.keySet()) {
            for (String key2 : nameToNumber.keySet()) {
                if (key1.endsWith(key2.substring(0, 1))) {
                    joined.put(key1 + key2.substring(1), nameToNumber.get(key1) + nameToNumber.get(key2));
                }
            }
        }

        System.out.println(String.join("|", joined.keySet()));
        Pattern pattern = Pattern.compile(String.join("|", nameToNumber.keySet()));
        Pattern joinedP = Pattern.compile(String.join("|", joined.keySet()));

        int part1 = input.lines()
                .map(line -> line.replaceAll("[a-z]", ""))
                .mapToInt(line -> Integer.parseInt(line.substring(0, 1)) * 10 + Integer.parseInt(line.substring(line.length() - 1, line.length())))
                .sum();
//        int part1 = 0;
        for (String line : input.lines().toArray(String[]::new)) {
            if (line.contains("oneight")) {
                Pattern joinedTest = Pattern.compile(String.join("|", joined.keySet()));
                Matcher matcher = joinedTest.matcher(line);
                String finalLine = matcher.replaceAll(result -> {
                    System.out.println(result.group());
                    return joined.get(result.group());
                });
                System.out.println(line);
                System.out.println(finalLine);
            }
        }
        int part2 = input.lines()
                .map(joinedP::matcher)
                .map(matcher -> matcher.replaceAll(result -> joined.get(result.group())))
                .map(pattern::matcher)
                .map(matcher -> matcher.replaceAll(result -> nameToNumber.get(result.group())))
                .map(line -> line.replaceAll("[a-z]", ""))
                .mapToInt(line -> Integer.parseInt(line.substring(0, 1)) * 10 + Integer.parseInt(line.substring(line.length() - 1, line.length())))
                .sum();
        System.out.println(Arrays.toString(input.lines()
                .map(pattern::matcher)
                .map(matcher -> matcher.replaceAll(result -> nameToNumber.get(result.group())))
                .map(line -> line.replaceAll("[a-z]", ""))
                .mapToInt(line -> Integer.parseInt(line.substring(0, 1)) * 10 + Integer.parseInt(line.substring(line.length() - 1, line.length())))
                .toArray()));
        return new Result(part1, part2);
    }

}
