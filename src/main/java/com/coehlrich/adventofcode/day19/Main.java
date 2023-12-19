package com.coehlrich.adventofcode.day19;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        String[] halves = input.split("\n\n");
        List<Part> parts = halves[1].lines().map(Part::parse).toList();
        Map<String, Workflow> workflows = halves[0].lines()
                .map(Workflow::parse)
                .collect(Collectors.toMap(Workflow::name, Function.identity()));

        List<Part> accepted = new ArrayList<>();
        for (Part part : parts) {
            String name = "in";
            while (!name.equals("A") && !name.equals("R")) {
                Workflow workflow = workflows.get(name);
                for (Rule rule : workflow.rules()) {
                    if (rule.condition().test(rule.getter().applyAsInt(part))) {
                        name = rule.result();
                        break;
                    }
                }
            }

            if (name.equals("A")) {
                accepted.add(part);
            }
        }

        Part min = new Part(1, 1, 1, 1);
        Part max = new Part(4000, 4000, 4000, 4000);
        return new Result(
                accepted.stream().mapToInt(part -> part.x() + part.m() + part.a() + part.s()).sum(),
                getValues(workflows, workflows.get("in"), min, max));
    }

    public long getValues(Map<String, Workflow> workflows, Workflow current, Part min, Part max) {
        long result = 0;
        for (Rule rule : current.rules()) {
//            System.out.println("Entering: " + current.name() + ":\nmin: " + min + "\nmax: " + max);
            boolean valid = rule.type() == null;
            Part ruleMin = min;
            Part ruleMax = max;
            boolean end = false;
            if (!valid) {
                char type = rule.type().charAt(0);
                int minValue = rule.getter().applyAsInt(min);
                int maxValue = rule.getter().applyAsInt(max);
                if (rule.condition().test(minValue) || rule.condition().test(maxValue)) {
                    int requiredMin = switch (type) {
                        case '<' -> minValue;
                        case '>' -> Math.min(maxValue, rule.value() + 1);
                        default -> throw new IllegalArgumentException("Unexpected value: " + type);
                    };

                    int requiredMax = switch (type) {
                        case '<' -> Math.max(minValue, rule.value() - 1);
                        case '>' -> maxValue;
                        default -> throw new IllegalArgumentException("Unexpected value: " + type);
                    };

                    char source = rule.source().charAt(0);

                    ruleMin = switch (source) {
                        case 'x' -> new Part(requiredMin, min.m(), min.a(), min.s());
                        case 'm' -> new Part(min.x(), requiredMin, min.a(), min.s());
                        case 'a' -> new Part(min.x(), min.m(), requiredMin, min.s());
                        case 's' -> new Part(min.x(), min.m(), min.a(), requiredMin);
                        default -> throw new IllegalArgumentException("Unexpected value: " + source);
                    };

                    ruleMax = switch (source) {
                        case 'x' -> new Part(requiredMax, max.m(), max.a(), max.s());
                        case 'm' -> new Part(max.x(), requiredMax, max.a(), max.s());
                        case 'a' -> new Part(max.x(), max.m(), requiredMax, max.s());
                        case 's' -> new Part(max.x(), max.m(), max.a(), requiredMax);
                        default -> throw new IllegalArgumentException("Unexpected value: " + source);
                    };

                    int newMinValue = switch (type) {
                        case '<' -> rule.value() >= rule.getter().applyAsInt(min) ? rule.value() : -1;
                        case '>' -> rule.getter().applyAsInt(min);
                        default -> throw new IllegalArgumentException("Unexpected value: " + type);
                    };

                    int newMaxValue = switch (type) {
                        case '>' -> rule.value() <= rule.getter().applyAsInt(max) ? rule.value() : -1;
                        case '<' -> rule.getter().applyAsInt(max);
                        default -> throw new IllegalArgumentException("Unexpected value: " + type);
                    };

                    if (newMinValue == -1 || newMaxValue == -1) {
                        end = true;
                    }

                    min = switch (source) {
                        case 'x' -> new Part(newMinValue, min.m(), min.a(), min.s());
                        case 'm' -> new Part(min.x(), newMinValue, min.a(), min.s());
                        case 'a' -> new Part(min.x(), min.m(), newMinValue, min.s());
                        case 's' -> new Part(min.x(), min.m(), min.a(), newMinValue);
                        default -> throw new IllegalArgumentException("Unexpected value: " + source);
                    };

                    max = switch (source) {
                        case 'x' -> new Part(newMaxValue, max.m(), max.a(), max.s());
                        case 'm' -> new Part(max.x(), newMaxValue, max.a(), max.s());
                        case 'a' -> new Part(max.x(), max.m(), newMaxValue, max.s());
                        case 's' -> new Part(max.x(), max.m(), max.a(), newMaxValue);
                        default -> throw new IllegalArgumentException("Unexpected value: " + source);
                    };
                }
            }
            if (rule.result().equals("A")) {
                result += (long) ((ruleMax.x() - ruleMin.x()) + 1) * (long) ((ruleMax.m() - ruleMin.m()) + 1) * (long) ((ruleMax.a() - ruleMin.a()) + 1) * (long) ((ruleMax.s() - ruleMin.s()) + 1);
//                System.out.println((long) ((ruleMax.x() - ruleMin.x()) + 1) * (long) ((ruleMax.m() - ruleMin.m()) + 1) * (long) ((ruleMax.a() - ruleMin.a()) + 1) * (long) ((ruleMax.s() - ruleMin.s()) + 1));
            } else if (!rule.result().equals("R")) {
                result += getValues(workflows, workflows.get(rule.result()), ruleMin, ruleMax);
            }
//            System.out.println("Exiting: " + current.name() + ":\nmin: " + min + "\nmax: " + max);
            if (end) {
                return result;
            }
        }
        return result;
    }

}
