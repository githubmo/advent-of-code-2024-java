package tech.mozek.ex2;

import com.google.common.collect.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Solution to sample is {}", solution(ReadInput.readSampleInput()));
            logger.info("Solution to input is {}", solution(ReadInput.readInput()));
        } catch (IOException e) {
            logger.error("Error reading input", e);
            throw new RuntimeException(e);
        }
    }

    public static int solution(Stream<String> input) throws IOException {
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
}