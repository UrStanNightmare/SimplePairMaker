package ru.aston.oshchepkov_aa.simplepairmaker.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
    private LoggerUtils() {}

    public static void turnOffConsoleAppenders(){
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        var loggers = loggerContext.getLoggerList();

        for (Logger logger : loggers) {
            var appender = logger.getAppender("Console");
            if (appender != null){
                appender.addFilter(new DenyFilter());
            }
        }
    }
}
