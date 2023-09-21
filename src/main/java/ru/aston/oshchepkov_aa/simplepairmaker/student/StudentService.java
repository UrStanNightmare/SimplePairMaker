package ru.aston.oshchepkov_aa.simplepairmaker.student;

import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.StudentPair;

import java.util.List;

public interface StudentService {
    List<StudentPair> generateStudentPairs(List<String> students, int questionsCount);
}
