package cn.edu.buaa.patpat.boot.modules.course.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Announcement extends HasCreatedAndUpdated {
    private int id;
    private int courseId;

    /**
     * Account ID of the author.
     */
    private int accountId;

    private String title;
    private String content;

    private boolean topped;
}
