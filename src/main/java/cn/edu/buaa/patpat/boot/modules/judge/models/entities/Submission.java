package cn.edu.buaa.patpat.boot.modules.judge.models.entities;

import cn.edu.buaa.patpat.boot.modules.judge.models.JudgeTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Submission extends JudgeTimestamp implements Serializable {
    private int id;
    private int accountId;
    private String buaaId;
    private int courseId;
    private int problemId;

    private String language;
    private int score;
    private String data;

    /**
     * If a submission has not been finalized after 1 hour, it is considered timed out.
     */
    @JsonIgnore
    public boolean isTimedOut() {
        if (getEndTime() == null) {
            return getSubmitTime().plusHours(1).isBefore(LocalDateTime.now());
        }
        return false;
    }
}
