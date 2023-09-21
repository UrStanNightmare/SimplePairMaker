package ru.aston.oshchepkov_aa.simplepairmaker.view;

import lombok.extern.slf4j.Slf4j;
import ru.aston.oshchepkov_aa.simplepairmaker.charset.CharsetIdentifier;

import java.io.PrintStream;

@Slf4j
public class ConsoleView implements View {
    private static final PrintStream CONSOLE_STREAM = System.out;
    @Override
    public void printResult(String result) {
        log.info("Result print:");

        var ps = new PrintStream(CONSOLE_STREAM, false, CharsetIdentifier.getConsoleCharset());
        ps.println(result);
        ps.flush();
    }
}
