package cn.edu.buaa.patpat.boot.modules.course.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AnnouncementView extends HasCreatedAndUpdated implements Serializable {
    private int id;

    private String author;

    private String title;
    private String content;

    private boolean topped;
}
