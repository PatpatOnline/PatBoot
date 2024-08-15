package cn.edu.buaa.patpat.boot.modules.group.models.views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class GroupMemberView {
    @JsonIgnore
    private int id;

    private int accountId;
    private String buaaId;
    private String name;
    private String avatar;

    @JsonIgnore
    private int groupId;

    private boolean owner;
    private int weight;
}
