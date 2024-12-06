package tech.mozek.day4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Part 1 solution to sample is {}", part1(ReadInput.readSampleInput()));
            logger.info("Part 1 solution to input is {}", part1(ReadInput.readInput()));
            logger.info("Part 2 solution to sample is {}", part2(ReadInput.readSampleInput()));
            logger.info("Part 2 solution to input is {}", part2(ReadInput.readInput()));
        } catch (IOException e) {
            logger.error("Error reading input", e);
            throw new RuntimeException(e);
        }
    }

    public static int part1(Stream<String> input) throws IOException {
        var befores = new HashMap<Integer, ArrayList<Integer>>();
        var afters = new HashMap<Integer, ArrayList<Integer>>();
        var mapped = input.map(line -> {
            if (line.isBlank()) return 0;
            if (line.contains("|")) {
                var left = Integer.parseInt(line.split("\\|")[0].trim());
                var right = Integer.parseInt(line.split("\\|")[1].trim());
                addToMap(befores, left, right);
                addToMap(afters, right, left);
                return 0;
            } else {
                var listOfInts = new ArrayList<>(Arrays.stream(line.trim().split(",")).map(Integer::parseInt).toList());
                var middle = listOfInts.get(listOfInts.size() / 2);
                while (!listOfInts.isEmpty()) {
                    var first = listOfInts.removeFirst();
                    if (listOfInts.stream().anyMatch(i -> afters.getOrDefault(first, new ArrayList<>(List.of())).contains(i)) || listOfInts.stream().anyMatch(i -> befores.getOrDefault(i, new ArrayList<>(List.of())).contains(first))) {
                        return 0;
                    }
                }
                return middle;
            }
        });
        return mapped.reduce(Integer::sum).orElse(0);
    }

    public static long part2(Stream<String> input) throws IOException {
        var befores = new HashMap<Integer, ArrayList<Integer>>();
        var afters = new HashMap<Integer, ArrayList<Integer>>();
        var mapped = input.map(line -> {
            if (line.isBlank()) return 0;
            if (line.contains("|")) {
                var left = Integer.parseInt(line.split("\\|")[0].trim());
                var right = Integer.parseInt(line.split("\\|")[1].trim());
                addToMap(befores, left, right);
                addToMap(afters, right, left);
                return 0;
            } else {
                var listOfInts = new ArrayList<>(Arrays.stream(line.trim().split(",")).map(Integer::parseInt).toList());
                while (!listOfInts.isEmpty()) {
                    var first = listOfInts.removeFirst();

                    if (listOfInts.stream().anyMatch(i -> afters.getOrDefault(first, new ArrayList<>(List.of())).contains(i)) || listOfInts.stream().anyMatch(i -> befores.getOrDefault(i, new ArrayList<>(List.of())).contains(first))) {
                        // recreate the list of ints
                        listOfInts = new ArrayList<>(Arrays.stream(line.trim().split(",")).map(Integer::parseInt).toList());
                        listOfInts.sort(new PuzzleComparator(befores, afters));

                        return listOfInts.get(listOfInts.size() / 2);
                    }
                }
                return 0;
            }
        });
        return mapped.reduce(Integer::sum).orElse(0);
    }


    private static void addToMap(HashMap<Integer, ArrayList<Integer>> map, int key, int value) {
        map.computeIfAbsent(key, k -> new ArrayList<>(List.of())).add(value);
    }

    static class PuzzleComparator implements Comparator<Integer> {

        private final HashMap<Integer, ArrayList<Integer>> befores;
        private final HashMap<Integer, ArrayList<Integer>> afters;

        public PuzzleComparator(HashMap<Integer, ArrayList<Integer>> befores,
                                HashMap<Integer, ArrayList<Integer>> afters) {
            this.befores = befores;
            this.afters = afters;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            if (befores.getOrDefault(o1, new ArrayList<>()).contains(o2)) return -1;
            if (afters.getOrDefault(o1, new ArrayList<>()).contains(o2)) return 1;
            return 0;
        }
    }
}