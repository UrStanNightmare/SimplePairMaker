package ru.aston.oshchepkov_aa.simplepairmaker.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class FileParserTest {
    private static final String INPUT_FILE_NAME = "st";
    private static final String EMPTY = "";
    private static final String NON_EXISTENT = "notExists.txt";
    @TempDir
    Path tempDirectory;

    @BeforeEach
    void beforeEach() throws IOException {
        var inputPath = getInputFile();
        if (Files.exists(inputPath)){
            Files.delete(inputPath);
        }
        Files.createFile(inputPath);
    }

    @Test
    void test_create_with_non_existent_path() {
        assertThatCode(()-> new FileParser(Path.of(NON_EXISTENT)))
                .as("Must not find a file.")
                .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    void test_create_with_empty_path() {
        assertThatCode(()-> new FileParser(Path.of(EMPTY)))
                .as("Must not find a file.")
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining("File not specified");
    }

    @Test
    void test_create_with_temp_file() {
        assertThatCode(()-> new FileParser(getInputFile()))
                .as("Must be ok")
                .doesNotThrowAnyException();
    }

    @Test
    void test_parse_with_correct_data() throws IOException {
        var studs = List.of("Вася Пупкин", "Влад Веселов");
        fillInputFile(studs);

        var p = new FileParser(getInputFile());
        var parsed = p.parseStudents();

        assertThat(parsed)
                .as("Must parse correctly")
                .isEqualTo(studs);
    }

    @Test
    void test_parse_with_corrupted_data() throws IOException {
        var studsCorrupted = List.of("1.   Вася      Пупкин         ", "30.Влад  Веселов     zz");
        var studsExpected = List.of("Вася Пупкин", "Влад Веселов");
        fillInputFile(studsCorrupted);

        var p = new FileParser(getInputFile());
        var parsed = p.parseStudents();

        assertThat(parsed)
                .as("Must parse correctly")
                .isEqualTo(studsExpected);
    }

    @Test
    void test_parse_with_empty_temp_file() throws FileNotFoundException {
        var p = new FileParser(getInputFile());

        List<String> result = p.parseStudents();
        assertThat(result)
                .as("Must return empty list.")
                .isEmpty();
    }

    Path getInputFile(){
        return tempDirectory.resolve(INPUT_FILE_NAME);
    }

    void fillInputFile(List<String> lines) throws IOException {
        Files.write(getInputFile(),lines);
    }
}