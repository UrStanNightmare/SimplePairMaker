package ru.aston.oshchepkov_aa.simplepairmaker.log;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ru.aston.oshchepkov_aa.simplepairmaker.charset.CharsetIdentifier;

public class PatternLayoutAutoCharsetEncoder extends PatternLayoutEncoder {
    @Override
    public void start() {
        this.setCharset(CharsetIdentifier.getConsoleCharset());
        super.start();
    }
}
