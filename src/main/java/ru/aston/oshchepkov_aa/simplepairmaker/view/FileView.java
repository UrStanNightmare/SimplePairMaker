package ru.aston.oshchepkov_aa.simplepairmaker.view;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileView implements View {
    private final Path outputFilePath;

    public FileView(String outputFilePath) throws IOException {
        this.outputFilePath = Paths.get(outputFilePath);

        if (!isFileExists()) {
            createOutputFile();
        }
        log.info("File view created.");
    }

    @Override
    public void printResult(String result) {
        log.info("Printing result to {}", outputFilePath);

        try {
            Files.writeString(outputFilePath, result, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to write to file. {}", e.getMessage());
        }
    }

    private boolean isFileExists() {
        return Files.exists(outputFilePath);
    }

    private void createOutputFile() throws IOException {
        var parent = outputFilePath.getParent();
        if (parent != null) {
            log.info("Creating subdirs.");
            Files.createDirectories(parent);
        }
        log.info("Creating answer file.");
        Files.createFile(outputFilePath);
    }
}
