package cn.edu.buaa.patpat.boot.modules.discussion.dto;

import cn.edu.buaa.patpat.boot.common.models.HasUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DiscussionUpdateDto extends HasUpdated {
    private int id;
    private int type;
    private String title;
    private String content;
}
