package tech.mozek.ex1;

import com.google.common.collect.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mozek.ReadInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public static long solution(Stream<String> input) throws IOException {
        var inputList = input.toList();
        logger.info("There are {} lines", inputList.size());
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        inputList.forEach(line -> {
            if (line.isBlank()) return;
            String[] split = line.split("\\s+");
            left.add(Integer.parseInt(split[0]));
            right.add(Integer.parseInt(split[1]));
        });
        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        return Streams.
                zip(left.stream(), right.stream(), (l, r) -> Math.abs(l - r))
                .reduce(Integer::sum).get();
    }
}