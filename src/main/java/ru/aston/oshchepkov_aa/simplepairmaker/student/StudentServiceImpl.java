package ru.aston.oshchepkov_aa.simplepairmaker.student;

import lombok.extern.slf4j.Slf4j;
import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.EvenGenerationStrategy;
import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.OddGenerationStrategy;
import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.PairGenerationStrategy;
import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.StudentPair;

import java.util.Collection;
import java.util.List;

@Slf4j
public class StudentServiceImpl implements StudentService {
    private static StudentServiceImpl service;
    private StudentServiceImpl() {
        log.info("Student service created.");
    }

    public static StudentServiceImpl getService(){
        if (service == null){
            service = new StudentServiceImpl();
        }
        return service;
    }

    @Override
    public List<StudentPair> generateStudentPairs(List<String> students, int questionsCount){
        log.info("Generating pairs.");

        if (questionsCount < 1){
            return List.of();
        }

        PairGenerationStrategy generationStrategy;
        if (isEvenElementsCount(students)){
            generationStrategy = new EvenGenerationStrategy();
        }else {
            generationStrategy = new OddGenerationStrategy();
        }

        return generationStrategy.createPairs(students, questionsCount);
    }

    private boolean isEvenElementsCount(Collection<?> collection){
        return collection.size() % 2 == 0;
    }
}
