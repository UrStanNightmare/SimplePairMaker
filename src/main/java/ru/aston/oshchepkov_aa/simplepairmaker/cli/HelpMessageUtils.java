package ru.aston.oshchepkov_aa.simplepairmaker.cli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class HelpMessageUtils {
    private HelpMessageUtils() {
    }

    public static String getHelpString(Options options) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            HelpFormatter f = new HelpFormatter();
            f.printHelp(pw, f.getWidth(), "PairMaker", "Options:", options,
                    f.getLeftPadding(), f.getDescPadding(), null, true);
            pw.flush();
            return sw.toString();
        } catch (IOException e) {
            log.error("Help message generation failed. {}", e.getMessage(), e);
        }

        return null;
    }
}
