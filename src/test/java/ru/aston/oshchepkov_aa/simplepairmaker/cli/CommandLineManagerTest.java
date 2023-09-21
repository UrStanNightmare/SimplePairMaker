package ru.aston.oshchepkov_aa.simplepairmaker.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class CommandLineManagerTest {
    private static CommandLineManager manager;
    private static final String[] NO_ARGS = new String[0];
    private static final String[] REQUIERED_OPTIONS = {"-f", "file.txt", "-q", "3"};
    private static final String[] REQUIERED_OPTIONS_WITH_OUTPUT_FILE = {"-f", "file.txt", "-q", "3", "-fOut", "a.txt"};
    private static final String[] REQUIERED_OPTIONS_WITH_OUTPUT_FILE_INCORRECT = {"-f", "file.txt", "-q", "3", "-fOut", "a.txt", "b.txt"};
    private static final String[] REQUIERED_OPTIONS_INCORRECT_INPUT_FILE_PATH = {"-f", "", "-q", "3", "-fOut", "a.txt", "b.txt"};
    private static final String[] REQUIERED_OPTIONS_INCORRECT_QUESTIONS_COUNT = {"-f", "file.txt", "-q", "0", "-fOut", "a.txt", "b.txt"};

    @BeforeAll
    static void beforeAll() {
        manager = new CommandLineManager(new DefaultParser(), AvailableOptions.packOptions());
    }

    @Test
    void test_parse_args_no_options() {
        assertThatCode(() -> {
            var line = manager.doArgsParsing(NO_ARGS);
        })
                .as("Must not let complete program without required options")
                .isInstanceOf(MissingOptionException.class)
                .hasMessageContaining("Missing required options");

    }

    @Test
    void test_parse_options_required_args() {
        final CommandLine[] line = new CommandLine[1];
        assertThatCode(() -> {
            line[0] = manager.doArgsParsing(REQUIERED_OPTIONS);
        })
                .as("No exceptions expected")
                .doesNotThrowAnyException();

        assertThat(line[0].getOptions().length)
                .as("Must have 2 options")
                .isEqualTo(REQUIERED_OPTIONS.length / 2);
    }

    @Test
    void test_parse_args_required_options_and_file_output() {
        final CommandLine[] line = new CommandLine[1];
        assertThatCode(() -> {
            line[0] = manager.doArgsParsing(REQUIERED_OPTIONS_WITH_OUTPUT_FILE);
        })
                .as("No exceptions expected")
                .doesNotThrowAnyException();

        assertThat(line[0].getOptions().length)
                .as("Must have 3 options")
                .isEqualTo(REQUIERED_OPTIONS_WITH_OUTPUT_FILE.length / 2);
    }

    @Test
    void test_parse_args_required_options_and_out_file_incorrect_args_ignored() {
        final CommandLine[] line = new CommandLine[1];
        assertThatCode(() -> {
            line[0] = manager.doArgsParsing(REQUIERED_OPTIONS_WITH_OUTPUT_FILE_INCORRECT);

        })
                .as("Must ignore extra argument.")
                .doesNotThrowAnyException();
        var args = line[0].getOptionValues(AvailableOptions.OUTPUT_FILE.getOption());

        assertThat(args)
                .as("Has only one arg")
                .hasSize(1);
    }

    @Test
    void test_parse_args_incorrect_input_file() {
        assertThatCode(() -> {
            var line = manager.doArgsParsing(REQUIERED_OPTIONS_INCORRECT_INPUT_FILE_PATH);
        })
                .as("Must not validate input file path.")
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("Validation of argument");
    }

    @Test
    void test_parse_args_incorrect_questions_count() {
        assertThatCode(() -> {
            var line = manager.doArgsParsing(REQUIERED_OPTIONS_INCORRECT_QUESTIONS_COUNT);
        })
                .as("Must not validate negative questions count.")
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("Validation of argument");

    }
}