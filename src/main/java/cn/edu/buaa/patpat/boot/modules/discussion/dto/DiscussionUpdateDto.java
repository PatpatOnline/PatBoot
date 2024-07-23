package cn.edu.buaa.patpat.boot.modules.discussion.dto;

import lombok.Data;

@Data
public class DiscussionUpdateDto {
    private int id;
    private int type;
    private String title;
    private String content;
}
