package tech.mozek.day11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
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

    public static long part1(Stream<String> input) throws IOException {
        return solution(input, 25);
    }

    private static String StringToIntToString(String s) {
        return String.valueOf(Integer.parseInt(s));
    }

    public static long part2(Stream<String> input) throws IOException {
        cache.clear();
        return Arrays.stream(input.findFirst().orElseThrow().split("\\s+"))
                .map(s -> solution(Stream.of(s), 75)).reduce(Long::sum).orElseThrow();
    }

    private static long solution(Stream<String> input, int steps) {
        var stoneLine = Arrays.stream(input.findFirst().orElseThrow().split("\\s+")).toList();
        return stoneLine.stream().map(s -> solution2(List.of(s), steps, 0)).reduce(Long::sum).orElseThrow();
    }

    private static record NumStepSize(String num, int step) {
    }

    private static final Map<NumStepSize, Long> cache = new HashMap<>();

    private static long solution2(List<String> stoneLine, int steps, int currentStep) {
        if (currentStep == steps) return stoneLine.size();
        Stream<List<String>> stoneLines = splitList(stoneLine, 1);
        var results = new ArrayList<Long>();
        stoneLines.forEach(l -> {
            var size = l.stream().map(s -> {
                var numStepSize = new NumStepSize(s, currentStep);
                if (cache.containsKey(numStepSize)) {
                    return cache.get(numStepSize);
                }
                if (s.equals("0")) {
                    var sss = solution2(List.of("1"), steps, currentStep + 1);
                    cache.put(numStepSize, sss);
                    return sss;
                }
                if (s.length() % 2 == 0) {
                    var sss = solution2(List.of(StringToIntToString(s.substring(0, s.length() / 2)), StringToIntToString(s.substring(s.length() / 2))), steps, currentStep + 1);
                    cache.put(numStepSize, sss);
                    return sss;
                }
                var sss = solution2(List.of(String.valueOf(Long.parseLong(s) * 2024)), steps, currentStep + 1);
                cache.put(numStepSize, sss);
                return sss;
            }).reduce(Long::sum).orElseThrow();

//        logger.info(stoneLine.stream().reduce((a, b) -> a + " " + b).orElseThrow());
            results.add(size);
        });
        return results.stream().reduce(Long::sum).orElseThrow();
    }

    public static <T> Stream<List<T>> splitList(List<T> list, int size) {
        int listSize = list.size();
        return IntStream.range(0, (listSize + size - 1) / size) // Determine the number of sublists
                .mapToObj(i -> list.subList(i * size, Math.min(listSize, (i + 1) * size))); // Create sublists
    }
}