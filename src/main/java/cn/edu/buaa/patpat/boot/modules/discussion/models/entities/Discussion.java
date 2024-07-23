package cn.edu.buaa.patpat.boot.modules.discussion.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Discussion extends HasCreatedAndUpdated {
    private int id;
    private Integer type;

    private int courseId;
    private int authorId;

    private String title;
    private String content;

    private boolean topped;
    private boolean starred;
}
