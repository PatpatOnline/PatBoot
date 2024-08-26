package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnnouncementBriefView implements Serializable {
    private int id;
    private String title;
    private String content;
}
