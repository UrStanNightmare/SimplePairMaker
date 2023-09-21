package ru.aston.oshchepkov_aa.simplepairmaker.parser;

import java.text.ParseException;
import java.util.List;

public interface Parser {
    List<String> parseStudents() throws ParseException;
}
