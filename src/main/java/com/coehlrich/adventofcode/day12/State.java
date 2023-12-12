package com.coehlrich.adventofcode.day12;

import it.unimi.dsi.fastutil.ints.IntList;

import java.util.List;

public record State(List<Type> row, IntList clues, int damaged) {

}
