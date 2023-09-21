package ru.aston.oshchepkov_aa.simplepairmaker.student.pair;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class EvenGenerationStrategy implements PairGenerationStrategy {
    @Override
    public List<StudentPair> createPairs(List<String> students, int questions) {
        log.info("Using even strategy.");
        var copy = new ArrayList<>(students);
        Collections.shuffle(copy);

        var result = new ArrayList<StudentPair>(copy.size() / 2);
        for (var i = 0; i < copy.size(); i += 2) {
            result.add(new StudentPair(copy.get(i), copy.get(i + 1), true));
        }

        log.info("Generation done. Got {} pairs.", result.size());
        return result;
    }
}
