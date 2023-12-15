package com.coehlrich.adventofcode.day15;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.ArrayList;
import java.util.List;

public record Step(String text) {
    public int hash(boolean label) {
        int hash = 0;
        String text = this.text;
        if (label) {
            text = getLabel()[0];
        }
        for (char character : text.toCharArray()) {
            hash += (int) character;
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }

    public String[] getLabel() {
        return text.split("(?==|-)");
    }

    public void parse(Int2ObjectMap<List<Lens>> boxes) {
        int hash = hash(true);
        List<Lens> lens = boxes.computeIfAbsent(hash, tmp -> new ArrayList<>());
        String[] split = getLabel();
        String label = split[0];
        String remaining = split[1];
        switch (remaining.substring(0, 1)) {
            case "=" -> {
                boolean found = false;
                for (int i = 0; i < lens.size(); i++) {
                    if (lens.get(i).label().equals(label)) {
                        found = true;
                        lens.set(i, new Lens(label, Integer.parseInt(remaining.substring(1))));
                        break;
                    }
                }
                if (!found) {
                    lens.add(new Lens(label, Integer.parseInt(remaining.substring(1))));
                }
            }
            case "-" -> {
                lens.removeIf(contained -> contained.label().equals(label));
            }
        }
    }
}
