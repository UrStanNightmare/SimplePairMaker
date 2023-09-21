package ru.aston.oshchepkov_aa.simplepairmaker.parser;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class FileParser implements Parser {
    private static final int UNDEFINED = -1;
    private final Path filePath;
    private int largestNameSequenceLength;

    public FileParser(Path filePath) throws FileNotFoundException {
        this.filePath = filePath;

        checkExistence(filePath);
    }

    @Override
    public List<String> parseStudents() {
        log.info("Parsing students.");

        try (var reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            var students = reader.lines()
                    .map(this::parseStudentName)
                    .toList();
            log.info("Parsing done. Got {} students.", students.size());
            return students;
        } catch (IOException e) {
            log.error("{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void checkExistence(Path filePath) throws FileNotFoundException {
        log.info("Checking file " + filePath + " existence.");
        if (filePath.toString().isEmpty()){
            throw new FileNotFoundException("File not specified!");
        }

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File " + filePath + " not found!");
        }
        log.info("File exists.");
    }

    private String parseStudentName(String raw) {
        var b = raw.trim();

        var nameTokenData = findNameTokenData(b);

        var surnameTokenData = findSurnameTokenData(b, nameTokenData.lastWhiteSpace + 1);

        var name = new StringBuilder(nameTokenData.getLineLength() + 1 + surnameTokenData.getLineLength())
                .append(b, nameTokenData.startIndex, nameTokenData.endIndex)
                .append(" ")
                .append(b, surnameTokenData.startIndex, surnameTokenData.endIndex)
                .toString();

        if (name.length() > largestNameSequenceLength) {
            largestNameSequenceLength = name.length();
        }

        log.info("-> {}", name);
        return name;
    }

    private TokenData findNameTokenData(String line) {
        var nameStartIndex = UNDEFINED;
        var nameEndIndex = UNDEFINED;
        var spaceBeforeSurname = UNDEFINED;
        var whitespaceMet = false;
        for (var i = 0; i < line.length(); i++) {
            var c = line.charAt(i);
            if (nameStartIndex == UNDEFINED && Character.isLetter(c)) {
                nameStartIndex = i;
            }
            if (whitespaceMet && Character.isLetter(c)) {
                spaceBeforeSurname = i - 1;
                break;
            }

            if (!whitespaceMet && nameStartIndex != UNDEFINED && Character.isWhitespace(c)) {
                nameEndIndex = i;
                spaceBeforeSurname = i;
                whitespaceMet = true;
            }
        }

        if (nameStartIndex == UNDEFINED || nameEndIndex == UNDEFINED || spaceBeforeSurname == UNDEFINED) {
            throw new ParseException("Name parsing failed for line " + line);
        }

        return new TokenData(nameStartIndex, nameEndIndex, spaceBeforeSurname);
    }

    private TokenData findSurnameTokenData(String line,
                                           int startPos) {
        var lastSurnameLetterIndex = UNDEFINED;

        for (var i = startPos; i < line.length(); i++) {
            var c = line.charAt(i);
            if (!Character.isLetter(c)) {
                lastSurnameLetterIndex = i;
                break;
            }
        }

        if (lastSurnameLetterIndex == UNDEFINED) {
            lastSurnameLetterIndex = line.length();
        }

        return new TokenData(startPos, lastSurnameLetterIndex);
    }

    class TokenData {
        private int startIndex;
        private int endIndex;
        private int lastWhiteSpace;

        private TokenData() {
        }

        private TokenData(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        private TokenData(int startIndex, int endIndex, int lastWhiteSpace) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.lastWhiteSpace = lastWhiteSpace;
        }

        private int getLineLength() {
            return endIndex - startIndex;
        }
    }
}
