package cn.edu.buaa.patpat.boot.modules.discussion.models.views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionAccountView {
    @JsonIgnore
    private int id;

    private String buaaId;
    private String name;
    private String avatar;
    private boolean isTa;
    private boolean isTeacher;
}
