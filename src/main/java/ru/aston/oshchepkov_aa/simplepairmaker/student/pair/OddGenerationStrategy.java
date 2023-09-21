package ru.aston.oshchepkov_aa.simplepairmaker.student.pair;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class OddGenerationStrategy implements PairGenerationStrategy {
    @Override
    public List<StudentPair> createPairs(List<String> students, int questions) {
        log.info("Using odd strategy.");

        if (students.size() < 2){
            return new ArrayList<>(0);
        }

        var copy = new ArrayList<>(students);
        Collections.shuffle(copy);

        var extraStudent = copy.get(copy.size() - 1);
        log.info("{} is extra student", extraStudent);
        copy.remove(extraStudent);

        var result = new ArrayList<StudentPair>(calculateResultCapacity(students.size(), questions));

        for (var i = 0; i < copy.size(); i += 2) {
            result.add(new StudentPair(copy.get(i), copy.get(i + 1), true));
        }

        Collections.shuffle(copy);
        for (var i = 0; i < questions; i++){
            result.add(new StudentPair(extraStudent, copy.get(i), false));
        }

        log.info("Generation done. Got {} pairs.", result.size());
        return result;
    }

    private int calculateResultCapacity(int elementCount, int questions) {
        return (elementCount - 1) / 2 + (questions);
    }
}
