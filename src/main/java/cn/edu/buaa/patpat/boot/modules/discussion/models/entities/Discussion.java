package cn.edu.buaa.patpat.boot.modules.discussion.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Discussion extends HasCreatedAndUpdated implements Serializable {
    private int id;
    private int type;

    private int courseId;
    private int authorId;

    private String title;
    private String content;

    private boolean topped;
    private boolean starred;
}
