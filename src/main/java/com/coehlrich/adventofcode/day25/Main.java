package com.coehlrich.adventofcode.day25;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.*;
import java.util.stream.Collectors;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Map<String, List<String>> connected = new HashMap<>(input.lines().map(Module::parse)
                .collect(Collectors.toMap(Module::name, Module::connected)));

        Map<String, List<String>> newConnected = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : connected.entrySet()) {
            newConnected.computeIfAbsent(entry.getKey(), name -> new ArrayList<>()).addAll(entry.getValue());
            for (String to : entry.getValue()) {
                newConnected.computeIfAbsent(to, name -> new ArrayList<>()).add(entry.getKey());
            }
        }
        connected = newConnected;

//        long count = connected.values().stream().flatMap(List::stream).count();

//        for (Map.Entry<String, List<String>> entry : connected.entrySet()) {
//            if (entry.getValue().size() > 5) {
//                System
//            }
//        }

        Object2IntMap<Set<String>> counts = new Object2IntOpenHashMap<>();
        List<String> names = new ArrayList<>(connected.keySet());
        for (int i = 0; i < names.size(); i++) {
            if (i % 100 == 0) {
                System.out.println(i);
            }
            String s1 = names.get(i);
//            System.out.println(i);
            follow(counts, connected, s1);
        }

        List<Object2IntMap.Entry<Set<String>>> list = counts.object2IntEntrySet().stream().sorted((e1, e2) -> e2.getIntValue() - e1.getIntValue()).toList().subList(0, 3);
        for (Object2IntMap.Entry<Set<String>> entry : list) {
            List<String> stringL = new ArrayList<>(entry.getKey());
            connected.get(stringL.get(0)).remove(stringL.get(1));
            connected.get(stringL.get(1)).remove(stringL.get(0));
        }
        IntList result = countGroup(connected);
//        System.out.println(countGroup(copy(connected)));
        return new Result(result.getInt(0) * result.getInt(1), "N/A");
    }

    private void follow(Object2IntMap<Set<String>> result, Map<String, List<String>> map, String from) {
        Queue<State> states = new LinkedList<>();
        states.add(new State(List.of(from), from));
        Set<String> found = new HashSet<>();
        int count = map.keySet().size() - 1;
        while (count > found.size()) {
            State s = states.poll();
            for (String visit : map.get(s.current())) {
                if (found.add(visit)) {
                    List<String> newV = new ArrayList<>(s.visited());
                    newV.add(visit);
                    states.add(new State(newV, visit));
                    List<String> visited = s.visited();
                    String previous = visited.get(0);
                    for (int i = 1; i < visited.size(); i++) {
                        Set<String> set = Set.of(previous, visited.get(i));
                        result.put(set, result.getInt(set) + 1);
                    }
                }
            }
        }
    }

//    private int calculate(Map<String, List<String>> connected, int i, long count) {
//        int loop = 0;
//        for (Map.Entry<String, List<String>> entry : connected.entrySet()) {
//            for (String value : entry.getValue()) {
//                Map<String, List<String>> map = copy(connected);
//                map.get(entry.getKey()).remove(value);
//                map.get(value).remove(entry.getKey());
//                if (i <= 1) {
//                    int result = calculate(map, i + 1, count);
//                    if (result != -1) {
//                        return result;
//                    }
//                } else {
//                    IntList check = countGroup(map);
//                    if (check.size() == 2) {
//                        return check.getInt(0) * check.getInt(1);
//                    }
//                }
//            }
//        }
//        return -1;
//    }

    private Map<String, List<String>> copy(Map<String, List<String>> map) {
        return map.entrySet().stream().map(entry -> Map.entry(entry.getKey(), new ArrayList<>(entry.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public IntList countGroup(Map<String, List<String>> connected) {
        IntList result = new IntArrayList();
        while (!connected.isEmpty()) {
            Set<String> included = new HashSet<>();
            Queue<String> toCheck = new LinkedList<>();
            String first = connected.keySet().iterator().next();
            toCheck.add(first);
            included.add(first);
            while (!toCheck.isEmpty()) {
                String name = toCheck.poll();
                List<String> check = connected.remove(name);
                if (check != null) {
                    for (String other : check) {
                        if (included.add(other)) {
                            toCheck.add(other);
                        }
                    }
                }
            }
            result.add(included.size());
        }
        return result;
    }

}
