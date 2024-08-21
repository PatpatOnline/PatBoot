package cn.edu.buaa.patpat.boot.modules.task.dto;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.FilenameUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskScoreDto extends HasCreatedAndUpdated {
    private int score;
    private boolean late;

    @JsonIgnore
    private String record;

    private String filename;

    public void initFilename() {
        if (record != null) {
            filename = FilenameUtils.getName(record);
        }
    }
}
