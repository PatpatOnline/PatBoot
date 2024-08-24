package cn.edu.buaa.patpat.boot.modules.judge.dto;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.modules.judge.models.JudgeTimestamp;
import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmitResponse extends JudgeTimestamp {
    private int id;
    private int problemId;
    private int courseId;

    private int score;
    private TestResult result;

    public WebSocketPayload<SubmitResponse> toWebSocketPayload() {
        return new WebSocketPayload<>(Globals.WS_SUBMIT, this);
    }
}
