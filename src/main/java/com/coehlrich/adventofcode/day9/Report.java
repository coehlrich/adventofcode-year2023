package com.coehlrich.adventofcode.day9;

import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.stream.Stream;

public record Report(IntList values) {
    public static Report parse(String line) {
        return new Report(new IntArrayList(
                Stream.of(line.split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray()));
    }

    public int calculate(boolean part2) {
        Stack<IntList> stack = new ObjectArrayList<>();
        stack.push(values);
        while (stack.top().intStream().anyMatch(num -> num != 0)) {
            IntList current = stack.top();
            IntList newList = new IntArrayList();
            for (int i = 0; i < current.size() - 1; i++) {
                newList.add(current.getInt(i + 1) - current.getInt(i));
            }
            stack.push(newList);
        }

        int last = 0;
        while (!stack.isEmpty()) {
            IntList current = stack.pop();
            if (part2) {
                last = current.getInt(0) - last;
            } else {
                last = current.getInt(current.size() - 1) + last;
            }
        }
        return last;
    }
}
