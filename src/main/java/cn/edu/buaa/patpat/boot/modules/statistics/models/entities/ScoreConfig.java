package cn.edu.buaa.patpat.boot.modules.statistics.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScoreConfig implements Serializable {
    private int courseId;
    private int labScore;
    private int iterScore;
    private int projScore;

    public int getTotalScore() {
        return labScore + iterScore + projScore;
    }
}
