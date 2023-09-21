package ru.aston.oshchepkov_aa.simplepairmaker.charset;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CharsetIdentifier {
    private static Charset consoleCharset = null;
    private CharsetIdentifier() {
    }

    public static Charset getConsoleCharset(){
        if (null == consoleCharset){
            var c = System.console();
            if (null == c){
                consoleCharset =  StandardCharsets.UTF_8;
            }else {
                consoleCharset = c.charset();
            }
        }
        return consoleCharset;
    }
}
