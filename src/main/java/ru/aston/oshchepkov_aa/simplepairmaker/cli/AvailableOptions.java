package ru.aston.oshchepkov_aa.simplepairmaker.cli;

import lombok.Getter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

public enum AvailableOptions {
    INPUT_FILE(
            Option.builder()
                    .option("f")
                    .longOpt("inputFile")
                    .hasArg()
                    .argName("file.txt")
                    .type(String.class)
                    .required()
                    .desc("Students list text file.")
                    .build(),
            args -> {
                if (args.length != 1) {
                    return false;
                }
                var filename = args[0];
                return null != filename && !filename.isBlank();
            }
    ),
    QUESTIONS_COUNT(
            Option.builder()
                    .option("q")
                    .longOpt("questionsCount")
                    .hasArg()
                    .argName("number")
                    .type(Integer.class)
                    .required()
                    .desc("Questions per student count. (>0)")
                    .build(),
            args -> {
                if (args.length != 1) {
                    return false;
                }
                var questions = (Integer.parseInt(args[0]));
                return questions > 0;
            }
    ),
    OUTPUT_FILE(
            Option.builder()
                    .option("fOut")
                    .longOpt("fileOut")
                    .hasArg()
                    .argName("output.txt")
                    .desc("Result output file path.")
                    .build(),
            args -> {
                var path = args[0];
                return null != path && !path.isBlank();
            }
    ),
    OUTPUT_CONSOLE(
            Option.builder()
                    .option("cOut")
                    .longOpt("consoleOut")
                    .numberOfArgs(0)
                    .desc("If result needed to be printed to console.")
                    .build(),
            null
    ),
    SILENT(
        Option.builder()
                .option("s")
                .longOpt("silent")
                .numberOfArgs(0)
                .desc("Turn off logs")
                .build(),
            null
    );

    @Getter
    private final Option option;
    private final ValidationFunc validationFunc;

    AvailableOptions(Option option, ValidationFunc validationFunc) {
        this.option = option;
        this.validationFunc = validationFunc;
    }

    public Optional<ValidationFunc> getValidationFunc() {
        return Optional.ofNullable(validationFunc);
    }

    public static Options packOptions() {
        return Arrays.stream(AvailableOptions.values())
                .map(AvailableOptions::getOption)
                .collect(Options::new, Options::addOption, (a, b) -> {
                });
    }

    public static AvailableOptions getOption(Option option) {
        if (null == option) {
            throw new NoSuchElementException("No option can be found with null key.");
        }

        return Arrays.stream(AvailableOptions.values())
                .filter(ao -> ao.option.equals(option))
                .findFirst().orElseThrow(() -> {
                    throw new NoSuchElementException("No options found");
                });
    }
}
