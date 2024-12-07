package tech.mozek.day6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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

    private static final Point UP = new Point(0, -1);
    private static final Point DOWN = new Point(0, 1);
    private static final Point LEFT = new Point(-1, 0);
    private static final Point RIGHT = new Point(1, 0);
    private static final Map<Character, Point> DIRECTIONS = Map.of(
            '^', UP,
            'v', DOWN,
            '<', LEFT,
            '>', RIGHT
    );
    private static final Map<Character, Character> NEXT_DIRECTION = Map.of(
            '^', '>',
            'v', '<',
            '<', '^',
            '>', 'v'
    );

    public static long part1(Stream<String> input) throws IOException {
        var map = input.map(String::toCharArray).toArray(char[][]::new);
        var gaurdLocation = new Point(0, 0);
        var gaurdDirection = '^';
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == gaurdDirection) {
                    gaurdLocation = new Point(j, i);
                    break;
                }
            }
        }
        var turned = 0;
        logger.info("Start index is {}", gaurdLocation);
        while (gaurdLocation.x >= 0 && gaurdLocation.y >= 0 && gaurdLocation.x < map[0].length && gaurdLocation.y < map.length) {
            map[gaurdLocation.y][gaurdLocation.x] = 'X';
            var step = DIRECTIONS.get(gaurdDirection);
            var nextLocation = step.move(gaurdLocation);
            if (nextLocation.x >= 0 && nextLocation.y >= 0 && nextLocation.x < map[0].length && nextLocation.y < map.length &&
                    map[nextLocation.y][nextLocation.x] == '#') {
                gaurdDirection = NEXT_DIRECTION.get(gaurdDirection);
                step = DIRECTIONS.get(gaurdDirection);
                nextLocation = step.move(gaurdLocation);
                turned++;

            }
            gaurdLocation = nextLocation;
//            if (map.length < 20) {
//                printMap(map);
//                System.out.println();
//            }
        }
        logger.info("Turned {} times", turned);
        return Arrays
                .stream(map)
                .map(l -> IntStream.range(0, l.length)
                        .mapToObj(i -> l[i]))
                .map(l -> l.filter(x -> x == 'X').count())
                .reduce(Long::sum).orElse(0L);
    }

    public static long part2(Stream<String> input) throws IOException {
        // brute forced but need to find a better solution
        return 0;
    }

    private record Point(Integer x, Integer y) {
        public Point move(Point direction) {
            return new Point(x + direction.x, y + direction.y);
        }
    }

    private static void printMap(char[][] map) {
        for (char[] chars : map) {
            System.out.println(chars);
        }
    }

    private static char[][] deepClone(char[][] map) {
        return Arrays.stream(map).map(char[]::clone).toArray(x -> map.clone());
    }
}