package com.coehlrich.adventofcode.day5;

import com.coehlrich.adventofcode.day5.Conversion.Converted;

import java.util.ArrayList;
import java.util.List;

public record MapConversion(String name, List<Conversion> conversions) {

    private static final String NAME_PATTERN = "\\w+-to-\\w+ map:";

    public static MapConversion parse(String text) {
        String name = text.lines()
                .filter(line -> line.matches(NAME_PATTERN))
                .findFirst().get().replace(" map:", "");

        List<Conversion> conversions = text.lines()
                .filter(line -> !line.matches(NAME_PATTERN))
                .map(Conversion::parse)
                .toList();
        return new MapConversion(name, conversions);
    }

    public long convert(long value) {
        return conversions.stream()
                .filter(conversion -> conversion.canConvert(value))
                .findFirst()
                .map(conversion -> conversion.convert(value))
                .orElse(value);
    }

    public List<Range> convert(List<Range> ranges) {
        List<Range> converted = new ArrayList<>();
        List<Range> unconverted = ranges;
        for (Conversion conversion : conversions) {
            List<Range> newUnconverted = new ArrayList<>();
            for (Range convert : unconverted) {
                Converted end = conversion.convert(convert);
                if (end.before() != null) {
                    newUnconverted.add(end.before());
                }

                if (end.converted() != null) {
                    converted.add(end.converted());
                }

                if (end.after() != null) {
                    newUnconverted.add(end.after());
                }
            }
            unconverted = newUnconverted;
        }
        converted.addAll(unconverted);
        return converted;
    }
}
