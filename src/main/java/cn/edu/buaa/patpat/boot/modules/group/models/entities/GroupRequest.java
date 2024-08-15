package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupRequest extends HasCreated {
    private int groupId;
    private int accountId;
    private String buaaId;
    private String name;
}
