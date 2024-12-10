package tech.mozek.day9;

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
        var listInput = Arrays.stream(input.toList().getFirst().split("")).map(Integer::parseInt).toList();
        Deque<Integer> disk = new LinkedList<>(listInput);
        int leftId = 0;
        int rightId = listInput.size() / 2;
        int idx = 0;
        long total = 0;
        boolean isEmptySlot = false;

        while (!disk.isEmpty()) {
            int size = disk.pollFirst();
            if (isEmptySlot) {
                for (int i = 0; i < size; i++) {
                    total += (long) idx * rightId;
                    idx++;
                    disk.addLast(disk.pollLast() - 1);
                    if (disk.peekLast() != null && disk.peekLast() == 0) {
                        for (int j = 0; j < 2; j++) {
                            disk.pollLast();
                        }
                        rightId--;
                    }
                }
            } else {
                for (int i = 0; i < size; i++) {
                    total += (long) idx * leftId;
                    idx++;
                }
                leftId++;
            }
            isEmptySlot = !isEmptySlot;
        }

        return total;
    }

    public static long part2(Stream<String> input) throws IOException {
        var listInput = Arrays.stream(input.toList().getFirst().split("")).map(Integer::parseInt).toList();
        List<int[]> disk = new ArrayList<>();

        for (int i = 0; i < listInput.size(); i++) {
            Integer num = listInput.get(i);
            Integer idx = (i % 2 == 0) ? i / 2 : null;
            int size = num;
            if (size > 0) {
                disk.add(new int[]{idx != null ? idx : -1, size});
            }
        }

        int i = 0;
        while (i < disk.size()) {
            i++;
            int[] entry = disk.get(disk.size() - i);
            int idx = entry[0];
            int size = entry[1];
            if (idx == -1) {
                continue;
            }

            for (int j = 0; j < disk.size() - i; j++) {
                int[] current = disk.get(j);
                if (current[0] == -1 && current[1] >= size) {
                    if (current[1] == size) {
                        current[0] = idx;
                    } else {
                        current[1] -= size;
                        disk.add(j, new int[]{idx, size});
                    }
                    entry[0] = -1;
                    break;
                }
            }
        }

        long total = 0;
        long idx = 0;
        for (int[] entry : disk) {
            if (entry[0] != -1) {
                total += entry[0] * entry[1] * (idx + (entry[1] - 1) / 2.0);
            }
            idx += entry[1];
        }

        return total;
    }
}