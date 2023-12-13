package com.coehlrich.adventofcode.day13;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import java.util.List;
import java.util.stream.Stream;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<Map> maps = Stream.of(input.split("\n\n"))
                .map(Map::parse)
                .toList();
        return new Result(
                maps
                        .stream()
                        .mapToInt(map -> map.calculate(false))
                        .sum(),
                maps
                        .stream()
                        .mapToInt(map -> map.calculate(true))
                        .sum());
    }

}
