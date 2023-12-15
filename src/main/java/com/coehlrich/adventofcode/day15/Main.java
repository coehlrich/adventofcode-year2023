package com.coehlrich.adventofcode.day15;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.List;
import java.util.stream.Stream;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<Step> steps = Stream.of(input.replace("\n", "").split(","))
                .map(Step::new)
                .toList();

        Int2ObjectMap<List<Lens>> boxes = new Int2ObjectOpenHashMap<>();
        steps.forEach(step -> step.parse(boxes));

        int part2 = 0;
        for (int box : boxes.keySet()) {
            List<Lens> lenses = boxes.get(box);
            for (int i = 0; i < lenses.size(); i++) {
                part2 += (box + 1) * (i + 1) * lenses.get(i).focal();
            }
        }
        return new Result(
                steps.stream()
                        .mapToInt(step -> step.hash(false))
                        .sum(),
                part2);
    }

}
