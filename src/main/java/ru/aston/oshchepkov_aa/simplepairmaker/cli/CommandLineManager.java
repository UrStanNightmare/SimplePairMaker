package ru.aston.oshchepkov_aa.simplepairmaker.cli;

import org.apache.commons.cli.*;

import java.util.NoSuchElementException;

public class CommandLineManager {
    private final CommandLineParser parser;
    private final Options options;

    public CommandLineManager(CommandLineParser parser, Options options) {
        this.parser = parser;
        this.options = options;
    }

    public CommandLine doArgsParsing(String[] args) throws ParseException {
        var commandLine = parser.parse(options, args);

        validateOptions(commandLine.getOptions());
        return commandLine;
    }

    private void validateOptions(Option[] options) throws ParseException {
        try {
            for (Option option : options) {
                var validationFunc = AvailableOptions
                        .getOption(option)
                        .getValidationFunc();

                validationFunc.ifPresent(f -> {
                    var test = f.isArgumentsValid(option.getValues());
                    if (!test) {
                        throw new ValidationException("Validation of argument [" + option.getArgName()
                                + "] of option [" + option.getLongOpt() + "] failed.");
                    }
                });
            }
        } catch (NoSuchElementException | ValidationException e) {
            throw new ParseException(e.getMessage());
        }

    }
}
