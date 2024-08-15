package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionFilter {
    private String query;
    private Integer type;
}
