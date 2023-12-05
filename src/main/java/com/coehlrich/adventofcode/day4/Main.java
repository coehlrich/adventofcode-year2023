package com.coehlrich.adventofcode.day4;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.List;
import java.util.stream.Stream;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        record Card(IntList winning, IntList gotten) {
        }

        List<Card> cards = input.lines()
                .map(line -> line.split(": ")[1])
                .map(line -> line.split(" \\| "))
                .map(Stream::of)
                .map(stream -> stream
                        .map(list -> list.split(" "))
                        .map(Stream::of)
                        .map(list -> list
                                .filter(entry -> entry.length() > 0)
                                .mapToInt(Integer::parseInt)
                                .toArray())
                        .map(IntArrayList::new)
                        .toList())
                .map(list -> new Card(list.get(0), list.get(1)))
                .toList();

        Int2IntMap part2 = new Int2IntOpenHashMap();
        for (int i = 0; i < cards.size(); i++) {
            part2.put(i, 1);
        }
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            int winnings = 0;
            for (int gotten : card.gotten) {
                if (card.winning.contains(gotten)) {
                    winnings++;
                    if (i + winnings < cards.size()) {
                        part2.put(i + winnings, part2.get(i + winnings) + part2.get(i));
                    }
                }
            }
        }
        return new Result(cards.stream().mapToInt(card -> {
            int count = 0;
            for (int value : card.gotten) {
                if (card.winning.contains(value)) {
                    if (count == 0) {
                        count = 1;
                    } else {
                        count *= 2;
                    }
                }
            }
            return count;
        }).sum(), part2.values().intStream().sum());
    }

}
