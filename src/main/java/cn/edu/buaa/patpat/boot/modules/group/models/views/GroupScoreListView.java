package cn.edu.buaa.patpat.boot.modules.group.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.FilenameUtils;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupScoreListView extends HasCreatedAndUpdated implements Serializable {
    private int groupId;
    private int score;

    @JsonIgnore
    private String record;

    private String filename;

    public void initFilename() {
        if (record != null) {
            filename = FilenameUtils.getName(record);
        }
    }
}
