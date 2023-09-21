package ru.aston.oshchepkov_aa.simplepairmaker.student.pair.prettifier;

import ru.aston.oshchepkov_aa.simplepairmaker.student.pair.StudentPair;

import java.util.List;

public class ArrowPrettifier implements PairPrettifier {
    private static final String ARROW_BIDIRECTIONAL = "<----->";
    private static final String ARROW_UNIDIRECTIONAL = "<------";
    private static final String STUDENT_A = "Student A";
    private static final String STUDENT_B = "Student B";

    @Override
    public String createPrettyString(List<StudentPair> pairs) {
        var prettyBlockSize = calculateMaxLeftColumnLength(pairs);

        var builder = new StringBuilder();
        insertSimpleLine(builder, STUDENT_A, STUDENT_B, prettyBlockSize);

        for (var pair : pairs) {
            insertArrowLine(builder, pair, prettyBlockSize);
        }

        return builder.toString();
    }

    private int calculateMaxLeftColumnLength(List<StudentPair> pairs){
        var result = STUDENT_A.length();

        for (StudentPair pair : pairs) {
            var len = pair.studentA().length();
            if (len > result){
                result = len;
            }
        }
        return result;
    }

    private void insertPrettyBlock(StringBuilder builder, int size, String value, boolean isFinishing) {
        if (!isFinishing){
            if (size - value.length() > 0){
                builder.append(" ".repeat(size - value.length()));
            }
            builder.append(value);
            builder.append(" ");
        }else {
            builder.append(value);
        }
    }

    private void insertArrow(StringBuilder builder, boolean isBidirectional) {
        if (isBidirectional){
            builder.append(ARROW_BIDIRECTIONAL);
        }else {
            builder.append(ARROW_UNIDIRECTIONAL);
        }
        builder.append(" ");
    }

    private void insertArrowLine(StringBuilder builder, StudentPair pair, int size){
        insertPrettyBlock(builder, size, pair.studentA(), false);
        insertArrow(builder, pair.isBidirectional());
        insertPrettyBlock(builder, size, pair.studentB(), true);
        builder.append(System.lineSeparator());
    }

    private void insertSimpleLine(StringBuilder builder, String first, String second, int size){
        insertPrettyBlock(builder, size, first, false);
        builder.append(" ".repeat(ARROW_BIDIRECTIONAL.length() + 1));
        insertPrettyBlock(builder, size, second, true);
        builder.append(System.lineSeparator());
    }
}
