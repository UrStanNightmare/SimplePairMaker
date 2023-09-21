package ru.aston.oshchepkov_aa.simplepairmaker.student.pair.prettifier;

import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.StudentPair;

import java.util.List;

public interface PairPrettifier {
    String createPrettyString(List<StudentPair> pairs);
}
