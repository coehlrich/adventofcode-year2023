package com.coehlrich.adventofcode.day12;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import java.util.List;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<Row> part1 = input.lines().map(Row::parse).toList();
        return new Result(part1.stream().mapToLong(Row::possible).sum(), part1.stream().map(Row::convertPart2).mapToLong(Row::possible).sum());
    }

}
