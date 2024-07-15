package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;

@Data
public class TestCaseResult {
    private String flag;
    private String result;
    private int score;
}