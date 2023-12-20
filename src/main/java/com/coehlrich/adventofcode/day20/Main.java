package com.coehlrich.adventofcode.day20;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Utils;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Map<String, Module> modules = input.lines()
                .map(Module::parse)
                .collect(Collectors.toMap(Module::name, Function.identity()));

        Map<String, List<Module>> inputs = Stream.concat(modules.values()
                .stream()
                .map(Module::name), Stream.of("rx"))
                .map(module -> Map.entry(module, modules.values()
                        .stream()
                        .filter(module2 -> module2.outputs().contains(module))
                        .toList()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Object2BooleanMap<String> on = new Object2BooleanOpenHashMap<>();

        List<String> required = new ArrayList<>(inputs.get("rx")
                .stream()
                .map(Module::name)
                .map(inputs::get)
                .flatMap(List::stream)
                .map(Module::name)
                .toList());

        long low = 0;
        long high = 0;
        long part2 = 1;
        for (int i = 0; i < 1000 || !required.isEmpty(); i++) {
            Queue<Pulse> queue = new LinkedList<>();
            queue.add(new Pulse(null, "broadcaster", false));
            while (!queue.isEmpty()) {
                Pulse pulse = queue.poll();
                if (pulse.high() && i < 1000) {
                    high++;
                } else if (i < 1000) {
                    low++;
                }
                if (required.contains(pulse.source()) && pulse.high()) {
                    required.remove(pulse.source());
                    part2 = Utils.lcm(part2, i + 1);
//                    System.out.println(i);
//                    System.out.println(part2);
                }
                Module module = modules.get(pulse.name());
                if (module != null) {
                    switch (module.type()) {
                        case BROADCASTER -> module.outputs().stream().map(output -> new Pulse(module.name(), output, pulse.high())).forEach(queue::add);
                        case CONJUNCTION -> {
                            boolean nowOn = !inputs.get(pulse.name()).stream().map(Module::name).map(on::get).allMatch(bool -> bool != null && bool);
                            on.put(pulse.name(), nowOn);
                            module.outputs().stream().map(output -> new Pulse(module.name(), output, nowOn)).forEach(queue::add);
                        }
                        case FLIP_FLOP -> {
                            if (!pulse.high()) {
                                boolean nowOn = !on.getBoolean(pulse.name());
                                on.put(pulse.name(), nowOn);
                                module.outputs().stream().map(output -> new Pulse(module.name(), output, nowOn)).forEach(queue::add);
                            }
                        }
                    }
                }
            }
        }
        return new Result(low * high, part2); // calculateRX(modules, inputs, "rx", false));
    }

//    private Set<String> visited = new HashSet<>();
//
//    private long calculateRX(Map<String, Module> modules, Map<String, List<Module>> inputs, String module, boolean expected) {
//        Type type = modules.containsKey(module) ? modules.get(module).type() : null;
//        if (!visited.add(module)) {
//            return Long.MAX_VALUE;
//        }
//        if (type == null) {
//            long min = Long.MAX_VALUE;
//            for (Module input : inputs.get(module)) {
//                min = Math.min(calculateRX(modules, inputs, input.name(), expected), min);
//            }
//            visited.remove(module);
//            return min;
//        } else if (type == Type.BROADCASTER && !expected) {
//            visited.remove(module);
//            return 1;
//        } else if (type == Type.FLIP_FLOP && expected) {
//            long min = Long.MAX_VALUE;
//            for (Module input : inputs.get(module)) {
//                min = Math.min(calculateRX(modules, inputs, input.name(), false), min);
//            }
//            visited.remove(module);
//            return min;
//        } else if (type == Type.FLIP_FLOP && !expected) {
//            long min = Long.MAX_VALUE;
//            for (Module input : inputs.get(module)) {
//                min = Math.min(calculateRX(modules, inputs, input.name(), false), min);
//            }
//            visited.remove(module);
//            return min * 2;
//        } else if (type == Type.CONJUNCTION && !expected) {
//            long value = -1;
//            for (Module input : inputs.get(module)) {
//                long result = calculateRX(modules, inputs, input.name(), true);
//                if (result == Long.MAX_VALUE) {
//                    throw new IllegalStateException();
//                } else {
//                    value = value == -1 ? result : Utils.lcm(result, value);
//                }
//            }
//            visited.remove(module);
//            return value * 2;
//        } else if (type == Type.CONJUNCTION && expected) {
//            long min = Long.MAX_VALUE;
//            for (Module input : inputs.get(module)) {
//                min = Math.min(calculateRX(modules, inputs, input.name(), true), min);
//            }
//            visited.remove(module);
//            return min;
//        }
//        throw new IllegalStateException(type.toString() + " " + module + " " + expected);
//    }

}
