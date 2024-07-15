package cn.edu.buaa.patpat.boot.modules.problem.models.entities;

import lombok.Data;

@Data
public class CaseDescriptor {
    private int id;
    private String description;
    private int score;
}
