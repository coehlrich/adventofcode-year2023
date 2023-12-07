package com.coehlrich.adventofcode.day7;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;

import java.util.List;

public class Main implements Day {

    private static CharList STRENGTH_PART1 = new CharArrayList("AKQJT98765432".toCharArray());
    private static CharList STRENGTH_PART2 = new CharArrayList("AKQT98765432J".toCharArray());

    @Override
    public Result execute(String input) {
        List<Hand> hands = input.lines().map(Hand::parse).toList();
        return new Result(
                calculate(hands.stream()
                        .sorted((t, o) -> this.compareHands(t, o, false))
                        .toList()),
                calculate(hands.stream()
                        .sorted((t, o) -> this.compareHands(t, o, true))
                        .toList()));
    }

    private long calculate(List<Hand> hands) {
        long result = 0;
        for (int i = 0; i < hands.size(); i++) {
            result += hands.get(i).bid() * (i + 1);
        }
        return result;
    }

    public int compareHands(Hand t, Hand o, boolean part2) {
        Type tType = t.getType(part2);
        Type oType = o.getType(part2);

        if (tType != oType) {
            return oType.compareTo(tType);
        } else {
            CharList strength = part2 ? STRENGTH_PART2 : STRENGTH_PART1;
            for (int i = 0; i < t.characters().length(); i++) {
                char tChar = t.characters().charAt(i);
                char oChar = o.characters().charAt(i);
                if (tChar != oChar) {
                    return strength.indexOf(oChar) - strength.indexOf(tChar);
                }
            }
        }
        return 0;
    }

}
