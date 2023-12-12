package com.coehlrich.adventofcode.day12;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record Row(List<Type> row, IntList clues) {
    public static Row parse(String line) {
        String[] parts = line.split(" ");
        List<Type> row = parts[0].chars()
                .mapToObj(character -> switch (character) {
                    case '.' -> Type.OPERATIONAL;
                    case '#' -> Type.DAMAGED;
                    case '?' -> Type.UNKNOWN;
                    default -> throw new IllegalArgumentException("Unexpected value: " + character);
                }).toList();
        IntList clues = new IntArrayList(Stream.of(parts[1].split(","))
                .mapToInt(Integer::parseInt)
                .toArray());

        return new Row(row, clues);
    }

    public Row convertPart2() {
        List<Type> newRow = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i > 0) {
                newRow.add(Type.UNKNOWN);
            }
            newRow.addAll(row);
        }

        IntList newClues = new IntArrayList();
        for (int i = 0; i < 5; i++) {
            newClues.addAll(clues);
        }
        return new Row(newRow, newClues);
    }

    public long possible() {
        List<Type> row = new ArrayList<>(row());
        row.add(Type.OPERATIONAL);
        Object2LongMap<State> states = new Object2LongOpenHashMap<>(new State[] { new State(row, clues, 0) }, new long[] { 1 });
        while (!states.keySet().stream().map(State::row).allMatch(List::isEmpty)) {
            Object2LongMap<State> newStates = new Object2LongOpenHashMap<>();
            for (Object2LongMap.Entry<State> entry : states.object2LongEntrySet()) {
                State state = entry.getKey();
                Type current = state.row().get(0);
                List<State> toAdd = switch (current) {
                    case OPERATIONAL -> operational(state);
                    case DAMAGED -> damaged(state);
                    case UNKNOWN -> unknown(state);
                };
                for (State newState : toAdd) {
                    newStates.put(newState, newStates.getLong(newState) + entry.getLongValue());
                }
            }
            states = newStates;
        }
        return states.object2LongEntrySet()
                .stream()
                .filter(entry -> entry.getKey().clues().isEmpty())
                .mapToLong(Object2LongMap.Entry::getLongValue)
                .sum();
    }

    private List<State> operational(State state) {
        List<Type> newRow = state.row().subList(1, state.row().size());
        if (state.damaged() == 0 || state.damaged() == state.clues().getInt(0)) {
            IntList newClues = state.clues();
            if (state.damaged() > 0) {
                newClues = newClues.subList(1, newClues.size());
            }
            return List.of(new State(newRow, newClues, 0));
        } else if (state.damaged() == 0) {
            return List.of(new State(newRow, state.clues(), 0));
        }
        return List.of();
    }

    private List<State> damaged(State state) {
        List<Type> newRow = state.row().subList(1, state.row().size());
        int damaged = state.damaged() + 1;
        if (state.clues().isEmpty() || damaged > state.clues().getInt(0)) {
            return List.of();
        }
        return List.of(new State(newRow, state.clues(), damaged));
    }

    private List<State> unknown(State state) {
        List<State> newStates = new ArrayList<>();
        newStates.addAll(operational(state));
        newStates.addAll(damaged(state));
        return newStates;
    }
}
