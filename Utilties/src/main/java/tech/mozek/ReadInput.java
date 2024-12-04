package tech.mozek;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

public class ReadInput {

    public static Stream<String> readFile(Path path) throws IOException {
        return Files.lines(path);
    }

    public static Stream<String> readFileFromResources(String fileName) throws IOException {
        Path path = Path.of(Objects.requireNonNull(ReadInput.class.getClassLoader().getResource(fileName)).getPath());
        return Files.lines(path);
    }

    public static Stream<String> readSampleInput() throws IOException {
        return readFileFromResources("sample_input.txt");
    }

    public static Stream<String> readInput() throws IOException {
        return readFileFromResources("input.txt");
    }
}
