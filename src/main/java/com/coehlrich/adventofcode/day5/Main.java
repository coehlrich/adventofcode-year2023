package com.coehlrich.adventofcode.day5;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        String[] sections = input.split("\n\n");
        LongList seeds = new LongArrayList(Stream.of(sections[0].replace("seeds: ", "").split(" "))
                .mapToLong(Long::parseLong)
                .toArray());

        List<MapConversion> conversions = Stream.of(Arrays.copyOfRange(sections, 1, sections.length))
                .map(MapConversion::parse)
                .toList();

        LongList converted = seeds;
        for (MapConversion conversion : conversions) {
            converted = new LongArrayList(converted.longStream()
                    .map(conversion::convert)
                    .toArray());
        }

        List<Range> ranges = Range.parseSeeds(sections[0]);
//        System.out.println(ranges);
        for (MapConversion conversion : conversions) {
            ranges = conversion.convert(ranges);
//            System.out.println(ranges);
        }
//        System.out.println(ranges);
        return new Result(converted.longStream().min().getAsLong(), ranges.stream().mapToLong(Range::start).min().getAsLong());
    }

}
