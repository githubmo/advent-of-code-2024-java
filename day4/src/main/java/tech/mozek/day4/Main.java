package tech.mozek.day4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.util.HashMap;
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
        var grid = input.map(String::toCharArray).toArray(char[][]::new);
        var map = new HashMap<String, String>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                map.put(i + "," + j, grid[i][j] + "");
            }
        }
        var sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'X' || grid[i][j] == 'S') {
                    var vertical = String.format("%s%s%s%s", map.getOrDefault(i + "," + j, ""), map.getOrDefault(i + "," + (j + 1), ""), map.getOrDefault(i + "," + (j + 2), ""), map.getOrDefault(i + "," + (j + 3), ""));
                    var horizontal = String.format("%s%s%s%s", map.getOrDefault(i + "," + j, ""), map.getOrDefault((i + 1) + "," + j, ""), map.getOrDefault((i + 2) + "," + j, ""), map.getOrDefault((i + 3) + "," + j, ""));
                    var bslash = String.format("%s%s%s%s", map.getOrDefault(i + "," + j, ""), map.getOrDefault((i + 1) + "," + (j + 1), ""), map.getOrDefault((i + 2) + "," + (j + 2), ""), map.getOrDefault((i + 3) + "," + (j + 3), ""));
                    var fslash = String.format("%s%s%s%s", map.getOrDefault(i + "," + j, ""), map.getOrDefault((i + 1) + "," + (j - 1), ""), map.getOrDefault((i + 2) + "," + (j - 2), ""), map.getOrDefault((i + 3) + "," + (j - 3), ""));
                    if (vertical.contains("XMAS") || vertical.contains("SAMX")) sum++;
                    if (horizontal.contains("XMAS") || horizontal.contains("SAMX")) sum++;
                    if (bslash.contains("XMAS") || bslash.contains("SAMX")) sum++;
                    if (fslash.contains("XMAS") || fslash.contains("SAMX")) sum++;
                }
            }
        }
        return sum;
    }

    public static long part2(Stream<String> input) throws IOException {
        var grid = input.map(String::toCharArray).toArray(char[][]::new);
        var map = new HashMap<String, String>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                map.put(i + "," + j, grid[i][j] + "");
            }
        }
        var sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'A') {
                    var bslash = String.format("%s%s%s", map.getOrDefault((i - 1) + "," + (j - 1), ""), "A", map.getOrDefault((i + 1) + "," + (j + 1), ""));
                    var fslash = String.format("%s%s%s", map.getOrDefault((i - 1) + "," + (j + 1), ""), "A", map.getOrDefault((i + 1) + "," + (j - 1), ""));
                    if ((bslash.contains("MAS") || bslash.contains("SAM")) && (fslash.contains("MAS") || fslash.contains("SAM")))
                        sum++;
                }
            }
        }
        return sum;
    }
}