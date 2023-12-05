package com.coehlrich.adventofcode.day5;

import java.util.stream.Stream;

public record Conversion(long destination, long source, long length) {
    public static Conversion parse(String text) {
        long[] values = Stream.of(text.split(" "))
                .mapToLong(Long::parseLong)
                .toArray();

        return new Conversion(values[0], values[1], values[2]);
    }

    public boolean canConvert(long value) {
        return source <= value && source + length > value;
    }

    public long convert(long value) {
        return destination + (value - source);
    }

    public Converted convert(Range range) {
        Range before = range.start() < source ? new Range(range.start(), Math.min(source - range.start(), range.length())) : null;
        long start = Math.max(range.start(), source);
        long end = Math.min(range.start() + range.length(), source + length);
        Range convert = start < source + length && end > source ? new Range(start, end - start) : null;
        Range converted = convert != null ? new Range(convert(convert.start()), convert.length()) : null;
        
        long finalValue = range.start() + range.length() - 1;
        long conversionFinalValue = source + length - 1;
        long startValue = Math.max(conversionFinalValue + 1, range.start());
        Range after = finalValue > conversionFinalValue ? new Range(startValue, (finalValue - startValue) + 1) : null;
        return new Converted(before, converted, after);
    }

    public static record Converted(Range before, Range converted, Range after) {

    }
}
