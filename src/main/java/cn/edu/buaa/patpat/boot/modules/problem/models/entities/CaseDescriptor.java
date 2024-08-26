package cn.edu.buaa.patpat.boot.modules.problem.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaseDescriptor implements Serializable {
    private int id;
    private String description;
    private int score;
}
