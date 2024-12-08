package tech.mozek.day7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info(Arrays.toString(generateArraysWithCombinators(3).stream().toArray()));
            logger.info("Part 1 solution to sample is {}", part1(ReadInput.readSampleInput()));
            logger.info("Part 1 solution to input is {}", part1(ReadInput.readInput()));
            logger.info("Part 2 solution to sample is {}", part2(ReadInput.readSampleInput()));
            logger.info("Part 2 solution to input is {}", part2(ReadInput.readInput()));
        } catch (IOException e) {
            logger.error("Error reading input", e);
            throw new RuntimeException(e);
        }
    }

    public static BigInteger part1(Stream<String> input) throws IOException {
       return  input
                .map(line -> line.split(": ")).map(line -> {
                    var expectedTotal = Long.parseLong(line[0].trim());
                    var list = Arrays.stream(line[1].trim().split("\\s+")).map(Long::parseLong).toList();
                    var operatorOptions = generateArrays(list.size() - 1);
                    return countPossibleCalibrations(expectedTotal, list, operatorOptions);
                }).map(BigInteger::valueOf).reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }



    public static BigInteger part2(Stream<String> input) throws IOException {
        return  input
                .map(line -> line.split(": ")).map(line -> {
                    var expectedTotal = Long.parseLong(line[0].trim());
                    var list = Arrays.stream(line[1].trim().split("\\s+")).map(Long::parseLong).toList();
                    var operatorOptions = generateArraysWithCombinators(list.size() - 1);
                    return countPossibleCalibrationsPart2(expectedTotal, list, operatorOptions);
                }).parallel().map(BigInteger::valueOf).reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    private static long countPossibleCalibrations(long expectedTotal, List<Long> list, List<String> operatorOptions) {
        var totalOptions = 0L;
        for(String operatorOption : operatorOptions) {
            var operators = new ArrayList<>(Arrays.stream(operatorOption.split(""))
                    .map(s -> s.charAt(0))
                    .toList());

            var listCopy = new ArrayList<>(list.stream().toList());// creating a deep clone
            var sum = listCopy.removeFirst();
            while (!listCopy.isEmpty()) {
                var i = listCopy.removeFirst();
                var o = operators.removeFirst();
                if (o == '+') {
                    sum += i;
                } else {
                    sum *= i;
                }
            }
            if (sum == expectedTotal){
                return expectedTotal;
            }
        }
        return totalOptions;
    }

    private static long countPossibleCalibrationsPart2(long expectedTotal, List<Long> list, List<List<String>> operatorOptions) {
        var totalOptions = 0L;
        for(var operators : operatorOptions) {
            var listCopy = new ArrayList<>(list.stream().toList());// creating a deep clone
            var sum = listCopy.removeFirst();
            while (!listCopy.isEmpty()) {
                var i = listCopy.removeFirst();
                var o = operators.removeFirst();
                if (Objects.equals(o, "+")) {
                    sum += i;
                } else if (Objects.equals(o, "*")){
                    sum *= i;
                } else {
                    sum = Long.parseLong("" + sum + i);
                }
            }
            if (sum == expectedTotal){
                return expectedTotal;
            }
        }
        return totalOptions;
    }

    private static List<String> generateArrays(int size) {
        if (size == 0) {
            List<String> base = new ArrayList<>();
            base.add(""); // List with an empty string
            return base;
        }

        List<String> smallerArrays = generateArrays(size - 1);
        List<String> results = new ArrayList<>();

        for (String array : smallerArrays) {
            results.add(array + "+");
            results.add(array + "*");
        }

        return results;
    }

    private static List<List<String>> generateArraysWithCombinators(int size) {
        if (size == 0) {
            List<List<String>> base = new ArrayList<>();
            base.add(Collections.emptyList()); // List with an empty string
            return base;
        }

        List<List<String>> smallerArrays = generateArraysWithCombinators(size - 1);
        List<List<String>> results = new ArrayList<>();

        for (List<String> list : smallerArrays) {
            results.add(Stream.concat(list.stream(), Stream.of("+"))
                    .collect(Collectors.toList()));
            results.add(Stream.concat(list.stream(), Stream.of("*"))
                    .collect(Collectors.toList()));
            results.add(Stream.concat(list.stream(), Stream.of("||"))
                    .collect(Collectors.toList()));
        }

        return results;
    }



}