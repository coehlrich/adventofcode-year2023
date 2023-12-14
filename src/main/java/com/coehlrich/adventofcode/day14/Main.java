package com.coehlrich.adventofcode.day14;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Type[][] map = input.lines()
                .map(line -> line.chars()
                        .mapToObj(character -> switch (character) {
                            case 'O' -> Type.ROUND;
                            case '#' -> Type.CUBE;
                            case '.' -> Type.EMPTY;
                            default -> throw new IllegalArgumentException("Unexpected value: " + character);
                        })
                        .toArray(Type[]::new))
                .toArray(Type[][]::new);

        return new Result(new Map(map).calculate(false), new Map(map).calculate(true));
    }

}
