package ru.aston.oshchepkov_aa.simplepairmaker.cli;

@FunctionalInterface
public interface ValidationFunc {
    boolean isArgumentsValid(String... args);
}
