package tech.mozek.ex2;

import com.google.common.collect.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static int part1(Stream<String> input) throws IOException {
        return input
                .map(line -> line.split("\\s+"))
                .map(line -> Arrays.stream(line).map(Integer::parseInt).toList())
                .map(line -> {
                    var diffs = Streams
                            .zip(line.stream(), line.stream().skip(1), (l, r) -> l - r).toList();
                    if (diffs.stream().allMatch(d -> d <= -1 && d >= -3)) return 1;
                    if (diffs.stream().allMatch(d -> d <= 3 && d >= 1)) return 1;
                    return 0;
                }).reduce(Integer::sum).get();
    }

    public static int part2(Stream<String> input) throws IOException {
        return input
                .map(line -> line.split("\\s+"))
                .map(line -> Arrays.stream(line).map(Integer::parseInt).toList())
                .map(line -> {
                    var hasSafeOption = Stream.concat(Stream.of(line), generatePermutations(line)).anyMatch(ll -> {
                        var diffs = Streams
                                .zip(ll.stream(), ll.stream().skip(1), (l, r) -> l - r).toList();
                        if (diffs.stream().allMatch(d -> d <= -1 && d >= -3)) return true;
                        return diffs.stream().allMatch(d -> d <= 3 && d >= 1);
                    });
                    return hasSafeOption ? 1 : 0;
                }).reduce(Integer::sum).get();
    }

    public static Stream<List<Integer>> generatePermutations(List<Integer> numbers) {
        return IntStream.range(0, numbers.size())
                .mapToObj(i -> {
                    List<Integer> permutation = new ArrayList<>(numbers);
                    permutation.remove(i);
                    return permutation;
                });
    }
}