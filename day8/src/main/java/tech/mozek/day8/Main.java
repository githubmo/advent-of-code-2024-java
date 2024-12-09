package tech.mozek.day8;

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

    public static long part1(Stream<String> input) throws IOException {
        // make input into a 2d array of chars
        var grid = input.map(String::toCharArray).toArray(char[][]::new);
        var map = new HashMap<Character, ArrayList<Index>>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++){
                if ( grid[i][j] != '.') {
                    map.computeIfAbsent(grid[i][j], k -> new ArrayList<>()).add(new Index(i, j));
                }
            }
        }
        var antinodes = new HashSet<Index>();
        for (var entry : map.entrySet()) {
            // we can have antinodes if we have at least two of the same antennas
            if (entry.getValue().size() > 1) {
                findUniquePairs(entry.getValue()).stream()
                        .flatMap(p -> Stream.of(extendBackward(p.first(), p.second()), extendForward(p.first(), p.second())))
                        .forEach(index -> {
                            if (index.i >= 0 && index.i < grid.length && index.j >= 0 && index.j < grid[index.i].length){
                                antinodes.add(index);
                            }
                        });
            }
        }
        return antinodes.size();
    }

    public static long part2(Stream<String> input) throws IOException {
        // make input into a 2d array of chars
        var grid = input.map(String::toCharArray).toArray(char[][]::new);
        var map = new HashMap<Character, ArrayList<Index>>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++){
                if ( grid[i][j] != '.') {
                    map.computeIfAbsent(grid[i][j], k -> new ArrayList<>()).add(new Index(i, j));
                }
            }
        }
        var antinodes = new HashSet<Index>();
        for (var entry : map.entrySet()) {
            // we can have antinodes if we have at least two of the same antennas
            if (entry.getValue().size() > 1) {
                findUniquePairs(entry.getValue()).stream()
                        .forEach(p -> {
                            var b1 = p.first();
                            var b2 = p.second();
                            var f1 = p.first();
                            var f2 = p.second();

                            while(b1.i >= 0 && b1.i < grid.length && b1.j >= 0 && b1.j < grid[0].length) {
                                antinodes.add(b1);
                                antinodes.add(b2);
                                var bb = extendBackward(b1, b2);
                                b2 = b1;
                                b1 = bb;
                            }

                            while(f2.i >= 0 && f2.i < grid.length && f2.j >= 0 && f2.j < grid[0].length) {
                                antinodes.add(f1);
                                antinodes.add(f2);
                                var ff = extendForward(f1, f2);
                                f1 = f2;
                                f2 = ff;
                            }

                        });
            }
        }
        return antinodes.size();
    }

    private static record Index(int i, int j){}

    // Method to calculate the point extending backward
    private static Index extendBackward(Index p1, Index p2) {
        int i = 2 * p1.i - p2.i;
        int j = 2 * p1.j - p2.j;
        return new Index(i, j);
    }

    // Method to calculate the point extending forward
    private static Index extendForward(Index p1, Index p2) {
        int i = 2 * p2.i - p1.i;
        int j = 2 * p2.j - p1.j;
        return new Index(i, j);
    }

    // Simple Pair class to hold a pair of objects
    private static record Pair<T>(T first, T second) {
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    private static <T> List<Pair<T>> findUniquePairs(List<T> list) {
        List<Pair<T>> pairs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                pairs.add(new Pair<>(list.get(i), list.get(j)));
            }
        }
        return pairs;
    }
}