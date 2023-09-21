package ru.aston.oshchepkov_aa.simplepairmaker.student;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudentServiceImplTest {
    static StudentService service = StudentServiceImpl.getService();
    @Test
    void test_generate_pairs_correct_even() {
        var studs = List.of("st1", "st2");

        var pairs = service.generateStudentPairs(studs, 1);

        assertThat(pairs)
                .as("Must generate x / 2 pairs")
                .hasSize(studs.size()/2);

        var pair = pairs.get(0);

        assertThat(pair.toString())
                .as("Must have all fields needed.")
                .containsSubsequence(studs.get(0))
                .containsSubsequence(studs.get(1))
                .containsSubsequence("isBidirectional=true");
    }

    @Test
    void test_generate_pairs_correct_odd() {
        var studs = List.of("st1", "st2", "st3");

        var pairs = service.generateStudentPairs(studs, 1);

        assertThat(pairs)
                .as("Must generate (x - 1) / 2 + q pairs")
                .hasSize((studs.size() - 1) / 2 + 1);
    }

    @Test
    void test_generate_pairs_empty_list() {
        var studs = new ArrayList<String>(0);

        var pairs = service.generateStudentPairs(studs, 1);

        assertThat(pairs)
                .as("Must be empty.")
                .isEmpty();
    }

    @Test
    void test_generate_pairs_with_incorrect_questions_count() {
        var studs = List.of("st1", "st2");

        var pairs = service.generateStudentPairs(studs, 0);

        assertThat(pairs)
                .as("Must be empty.")
                .isEmpty();
    }

    @Test
    void test_generate_pairs_odd_with_questions_count_equal_to_two() {
        var studs = List.of("st1", "st2", "st3");

        var pairs = service.generateStudentPairs(studs, 2);

        assertThat(pairs)
                .as("Must generate (x - 1) / 2 + q pairs")
                .hasSize((studs.size() - 1) / 2 + 2);
    }
}