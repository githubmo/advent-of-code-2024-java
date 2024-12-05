package tech.mozek.ex4;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
                .map(Main::findAllMultMatches)
                .map(s -> s.stream()
                        .map(Main::findAllDigits)
                        .map(l -> l.stream().reduce((a, b) -> a * b).orElse(0)))
                .map(s -> s.reduce(Integer::sum).orElse(0))
                .reduce(Integer::sum).orElse(0);
    }

    public static long part2(Stream<String> input) throws IOException {
        var sum = 0L;
        var s = Joiner.on("").join(input.toList());
        var matcher = doDontMultPattern.matcher(s);
        var canMult = true;
        while (matcher.find()) {
            var verb = matcher.group();
            if (verb.contains("don't")) {
                canMult = false;
            } else if (verb.contains("do")) {
                canMult = true;
            } else {
                if (canMult) {
                    sum += findAllDigits(verb).stream().reduce((a, b) -> a * b).orElse(0);
                }
            }
        }
        return sum;
    }

    public static Stream<List<Integer>> generatePermutations(List<Integer> numbers) {
        return IntStream.range(0, numbers.size())
                .mapToObj(i -> {
                    List<Integer> permutation = new ArrayList<>(numbers);
                    permutation.remove(i);
                    return permutation;
                });
    }

    private static final Pattern multPattern = Pattern.compile("mul\\(\\d+,\\d+\\)");
    private static final Pattern doDontMultPattern = Pattern.compile("(mul\\(\\d{1,3},\\d{1,3}\\))|(don't\\(\\))|(do\\(\\))");

    private static List<String> findAllMultMatches(String s) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = multPattern.matcher(s);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public static List<Integer> findAllDigits(String input) {
        List<String> digits = new ArrayList<>();
        Pattern digitPattern = Pattern.compile("\\d+"); // This pattern matches sequences of digits
        Matcher matcher = digitPattern.matcher(input);

        while (matcher.find()) {
            digits.add(matcher.group());
        }
        return digits.stream().mapToInt(Integer::parseInt).boxed().toList();
    }

    public static List<Integer> flattenList(Stream<Optional<Integer>> list) {
        return list.flatMap(Optional::stream).toList();
    }
}