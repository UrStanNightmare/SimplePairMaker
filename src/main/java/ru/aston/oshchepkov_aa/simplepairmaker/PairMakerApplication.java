package ru.aston.oshchepkov_aa.simplepairmaker;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import ru.aston.oshchepkov_aa.simplepairmaker.cli.AvailableOptions;
import ru.aston.oshchepkov_aa.simplepairmaker.cli.CommandLineManager;
import ru.aston.oshchepkov_aa.simplepairmaker.log.LoggerUtils;
import ru.aston.oshchepkov_aa.simplepairmaker.parser.FileParser;
import ru.aston.oshchepkov_aa.simplepairmaker.parser.Parser;
import ru.aston.oshchepkov_aa.simplepairmaker.student.StudentService;
import ru.aston.oshchepkov_aa.simplepairmaker.student.StudentServiceImpl;
import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.StudentPair;
import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.prettifier.ArrowPrettifier;
import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.prettifier.PairPrettifier;
import ru.aston.oshchepkov_aa.simplepairmaker.view.ConsoleView;
import ru.aston.oshchepkov_aa.simplepairmaker.view.FileView;
import ru.aston.oshchepkov_aa.simplepairmaker.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.aston.oshchepkov_aa.simplepairmaker.cli.HelpMessageUtils.getHelpString;

@Slf4j
public class PairMakerApplication {

    private final CommandLine commandLine;


    public static void main(String[] args) {
        try {
            var application = new PairMakerApplication(args);
            application.start();
        } catch (RuntimeException | ParseException e) {
            log.error("",e);
        }
    }

    public PairMakerApplication(String[] args) throws ParseException {
        try {
            var manager = new CommandLineManager(new DefaultParser(), AvailableOptions.packOptions());
            this.commandLine = manager.doArgsParsing(args);
        }catch (ParseException e){
            throw new ParseException(e.getMessage() +
                    System.lineSeparator() +
                    getHelpString(AvailableOptions.packOptions()));
        }
    }

    public void start() {
        try {
            checkLogsNeeded();

            log.info("Got {} options. [{}]",
                    commandLine.getOptions().length,
                    Arrays.stream(commandLine.getOptions())
                            .map(Option::getLongOpt)
                            .collect(Collectors.joining(", "))
            );

            var students = parseStudents();

            var pairs = generatePairs(students);

            var pairsString = createPrettyString(pairs);

            sendResultToViews(pairsString);
        }
        catch (Exception e) {
            log.error("Fatal exception. Can't continue", e);
            throw new RuntimeException(e);
        }
    }

    private void checkLogsNeeded(){
        if (commandLine.hasOption(AvailableOptions.SILENT.getOption())) {
            LoggerUtils.turnOffConsoleAppenders();
        }
    }

    private List<String> parseStudents() throws java.text.ParseException, FileNotFoundException {
        var inputFilePath = commandLine.getOptionValue(AvailableOptions.INPUT_FILE.getOption());

        Parser fileParser = new FileParser(Paths.get(inputFilePath));
        return fileParser.parseStudents();
    }

    private List<StudentPair> generatePairs(List<String> students){
        StudentService studentService = StudentServiceImpl.getService();
        var questionsCount = Integer.parseInt(
                commandLine.getOptionValue(AvailableOptions.QUESTIONS_COUNT.getOption()));
        return studentService.generateStudentPairs(students, questionsCount);
    }

    private String createPrettyString(List<StudentPair> pairs){
        PairPrettifier pairPrettifier = new ArrowPrettifier();
        return pairPrettifier.createPrettyString(pairs);
    }

    private void sendResultToViews(String result){
        List<View> views = new ArrayList<>();
        addViews(views);

        if (views.isEmpty()) {
            log.warn("No view specified! No result will be printed.");
        }

        views.forEach(v -> v.printResult(result));
    }

    private void addViews(List<View> views) {
        if (commandLine.hasOption(AvailableOptions.OUTPUT_FILE.getOption())) {
            var outputFilePath = commandLine.getOptionValue(AvailableOptions.OUTPUT_FILE.getOption());
            try {
                views.add(new FileView(outputFilePath));
            } catch (IOException e) {
                log.error("Failed to create file view. {}", e.getMessage(), e);
            }
        }

        if (commandLine.hasOption(AvailableOptions.OUTPUT_CONSOLE.getOption())) {
            views.add(new ConsoleView());
        }
    }
}
