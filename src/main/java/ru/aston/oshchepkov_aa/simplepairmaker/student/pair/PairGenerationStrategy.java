package ru.aston.oshchepkov_aa.simplepairmaker.student.pair;

import java.util.List;

public interface PairGenerationStrategy {
    List<StudentPair> createPairs(List<String> students, int questions);
}
