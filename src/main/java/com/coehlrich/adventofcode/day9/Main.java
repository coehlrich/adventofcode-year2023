package com.coehlrich.adventofcode.day9;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import java.util.List;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<Report> list = input.lines().map(Report::parse).toList();
        return new Result(
                list.stream().mapToInt(report -> report.calculate(false)).sum(),
                list.stream().mapToInt(report -> report.calculate(true)).sum());
    }

}
