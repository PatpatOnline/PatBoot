package cn.edu.buaa.patpat.boot.modules.problem.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProblemSelectView implements Serializable {
    private int id;
    private String title;
}
