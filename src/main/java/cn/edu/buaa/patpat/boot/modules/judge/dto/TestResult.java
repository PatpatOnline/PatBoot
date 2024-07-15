package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;

import java.util.List;

/**
 * Complete judge result for a problem.
 */
@Data
public class TestResult {
    public static String DEFAULT = """
            {"score":0,"fatal":true,"results":[{"flag":"FATAL","result":"JSON serialization error","score":0}]}
            """;

    /**
     * Total score of the submission.
     */
    private int score;
    /**
     * If some error occurs that prevent test cases from running, this field is set to true.
     * In this case, the {@code results} field contains only one element.
     */
    private boolean fatal;
    private List<TestCaseResult> results;
}
