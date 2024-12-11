package tech.mozek.day10;

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

    private record Index(int i, int j) {
    }

    public static long part1(Stream<String> input) throws IOException {
        Map<Index, HashSet<Index>> connects = new HashMap<>();

        var map = input.map(s ->
                Arrays.stream(s.split("")).filter(c -> !c.isBlank()).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) {
                    updateCount(connects, map, i, j, new Index(i, j));
                }
            }
        }

        return connects.values().stream().mapToInt(Set::size).sum();
    }

    private static void updateCount(Map<Index, HashSet<Index>> connects, int[][] map, int i, int j, Index start) {
        var current = map[i][j];
        if (current == 9) {
            var index = new Index(i, j);
            var currentConnects = connects.getOrDefault(start, new HashSet<>());
            currentConnects.add(index);
            connects.put(start, currentConnects);
            return;
        }
        var next = current + 1;
        if (i > 0 && map[i - 1][j] == next) {
            updateCount(connects, map, i - 1, j, start);
        }
        if (j > 0 && map[i][j - 1] == next) {
            updateCount(connects, map, i, j - 1, start);
        }
        if (i < map.length - 1 && map[i + 1][j] == next) {
            updateCount(connects, map, i + 1, j, start);
        }
        if (j < map[0].length - 1 && map[i][j + 1] == next) {
            updateCount(connects, map, i, j + 1, start);
        }
    }

    public static long part2(Stream<String> input) throws IOException {
        Map<Index, ArrayList<Index>> connects = new HashMap<>();

        var map = input.map(s ->
                Arrays.stream(s.split("")).filter(c -> !c.isBlank()).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) {
                    updateCount2(connects, map, i, j, new Index(i, j));
                }
            }
        }

        return connects.values().stream().mapToInt(List::size).sum();
    }

    private static void updateCount2(Map<Index, ArrayList<Index>> connects, int[][] map, int i, int j, Index start) {
        var current = map[i][j];
        if (current == 9) {
            var index = new Index(i, j);
            var currentConnects = connects.getOrDefault(start, new ArrayList<>());
            currentConnects.add(index);
            connects.put(start, currentConnects);
            return;
        }
        var next = current + 1;
        if (i > 0 && map[i - 1][j] == next) {
            updateCount2(connects, map, i - 1, j, start);
        }
        if (j > 0 && map[i][j - 1] == next) {
            updateCount2(connects, map, i, j - 1, start);
        }
        if (i < map.length - 1 && map[i + 1][j] == next) {
            updateCount2(connects, map, i + 1, j, start);
        }
        if (j < map[0].length - 1 && map[i][j + 1] == next) {
            updateCount2(connects, map, i, j + 1, start);
        }
    }
}