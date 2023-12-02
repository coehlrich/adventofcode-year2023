package com.coehlrich.adventofcode.day2;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import java.util.List;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        record Game(int id, int blue, int red, int green) {
        }
        List<Game> games = input.lines().map(line -> {
            String[] gameSplit = line.split(": ");
            int id = Integer.parseInt(gameSplit[0].replace("Game ", ""));
            int blue = 0;
            int red = 0;
            int green = 0;
            for (String shown : gameSplit[1].split("; ")) {
                for (String cube : shown.split(", ")) {
                    String[] cubeSplit = cube.split(" ");
                    int number = Integer.parseInt(cubeSplit[0]);
                    switch (cubeSplit[1]) {
                        case "blue" -> blue = Math.max(blue, number);
                        case "red" -> red = Math.max(red, number);
                        case "green" -> green = Math.max(green, number);
                    }
                }
            }
            return new Game(id, blue, red, green);
        }).toList();
        return new Result(
                games.stream().filter(game -> game.red() <= 12 && game.green() <= 13 && game.blue() <= 14).mapToInt(Game::id).sum(),
                games.stream().mapToInt(game -> game.red() * game.green() * game.blue).sum());
    }

}
